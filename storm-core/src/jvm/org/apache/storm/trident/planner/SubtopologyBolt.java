/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.storm.trident.planner;

import org.apache.storm.coordination.BatchOutputCollector;
import org.apache.storm.generated.GlobalStreamId;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DirectedSubgraph;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.apache.storm.trident.planner.processor.TridentContext;
import org.apache.storm.trident.state.State;
import org.apache.storm.trident.topology.BatchInfo;
import org.apache.storm.trident.topology.ITridentBatchBolt;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.trident.tuple.TridentTuple.Factory;
import org.apache.storm.trident.tuple.TridentTupleView.ProjectionFactory;
import org.apache.storm.trident.tuple.TridentTupleView.RootFactory;
import org.apache.storm.trident.util.TridentUtils;

// TODO: parameterizing it like this with everything might be a high deserialization cost if there's lots of tasks?
// TODO: memory problems?
// TODO: can avoid these problems by adding a boltfactory abstraction, so that boltfactory is deserialized once
//   bolt factory -> returns coordinatedbolt per task, but deserializes the batch bolt one time and caches
public class SubtopologyBolt implements ITridentBatchBolt {
    DirectedGraph _graph;
    Set<Node> _nodes;
    Map<String, InitialReceiver> _roots = new HashMap<>();
    Map<Node, Factory> _outputFactories = new HashMap<>();
    Map<String, List<TridentProcessor>> _myTopologicallyOrdered = new HashMap<>();
    Map<Node, String> _batchGroups;
    
    //given processornodes and static state nodes
    public SubtopologyBolt(DirectedGraph graph, Set<Node> nodes, Map<Node, String> batchGroups) {
        _nodes = nodes;
        _graph = graph;
        _batchGroups = batchGroups;
    }

    @Override
    public void prepare(Map conf, TopologyContext context, BatchOutputCollector batchCollector) {
        int thisComponentNumTasks = context.getComponentTasks(context.getThisComponentId()).size();
        for(Node n: _nodes) {
            if(n.stateInfo!=null) {
                State s = n.stateInfo.spec.stateFactory.makeState(conf, context, context.getThisTaskIndex(), thisComponentNumTasks);
                context.setTaskData(n.stateInfo.id, s);
            }
        }
        DirectedSubgraph<Node, Object> subgraph = new DirectedSubgraph(_graph, _nodes, null);
        TopologicalOrderIterator it = new TopologicalOrderIterator<>(subgraph);
        int stateIndex = 0;
        while(it.hasNext()) {
            Node n = (Node) it.next();
            if(n instanceof ProcessorNode) {
                ProcessorNode pn = (ProcessorNode) n;
                String batchGroup = _batchGroups.get(n);
                if(!_myTopologicallyOrdered.containsKey(batchGroup)) {
                    _myTopologicallyOrdered.put(batchGroup, new ArrayList());
                }
                _myTopologicallyOrdered.get(batchGroup).add(pn.processor);
                List<String> parentStreams = new ArrayList<>();
                List<Factory> parentFactories = new ArrayList<>();
                for(Node p: TridentUtils.getParents(_graph, n)) {
                    parentStreams.add(p.streamId);
                    if(_nodes.contains(p)) {
                        parentFactories.add(_outputFactories.get(p));
                    } else {
                        if(!_roots.containsKey(p.streamId)) {
                            _roots.put(p.streamId, new InitialReceiver(p.streamId, getSourceOutputFields(context, p.streamId)));
                        } 
                        _roots.get(p.streamId).addReceiver(pn.processor);
                        parentFactories.add(_roots.get(p.streamId).getOutputFactory());
                    }
                }
                List<TupleReceiver> targets = new ArrayList<>();
                boolean outgoingNode = false;
                for(Node cn: TridentUtils.getChildren(_graph, n)) {
                    if(_nodes.contains(cn)) {
                        targets.add(((ProcessorNode) cn).processor);
                    } else {
                        outgoingNode = true;
                    }
                }
                if(outgoingNode) {
                    targets.add(new BridgeReceiver(batchCollector));
                }
                
                TridentContext triContext = new TridentContext(
                        pn.selfOutFields,
                        parentFactories,
                        parentStreams,
                        targets,
                        pn.streamId,
                        stateIndex,
                        batchCollector
                        );
                pn.processor.prepare(conf, context, triContext);
                _outputFactories.put(n, pn.processor.getOutputFactory());
            }   
            stateIndex++;
        }        
        // TODO: get prepared one time into executor data... need to avoid the ser/deser
        // for each task (probably need storm to support boltfactory)
    }

    private Fields getSourceOutputFields(TopologyContext context, String sourceStream) {
        for(GlobalStreamId g: context.getThisSources().keySet()) {
            if(g.get_streamId().equals(sourceStream)) {
                return context.getComponentOutputFields(g);
            }
        }
        throw new RuntimeException("Could not find fields for source stream " + sourceStream);
    }
    
    @Override
    public void execute(BatchInfo batchInfo, Tuple tuple) {
        String sourceStream = tuple.getSourceStreamId();
        InitialReceiver ir = _roots.get(sourceStream);
        if(ir==null) {
            throw new RuntimeException("Received unexpected tuple " + tuple.toString());
        }
        ir.receive((ProcessorContext) batchInfo.state, tuple);
    }

    @Override
    public void finishBatch(BatchInfo batchInfo) {
        for(TridentProcessor p: _myTopologicallyOrdered.get(batchInfo.batchGroup)) {
            p.finishBatch((ProcessorContext) batchInfo.state);
        }
    }

    @Override
    public Object initBatchState(String batchGroup, Object batchId) {
        ProcessorContext ret = new ProcessorContext(batchId, new Object[_nodes.size()]);
        for(TridentProcessor p: _myTopologicallyOrdered.get(batchGroup)) {
            p.startBatch(ret);
        }
        return ret;
    }

    @Override
    public void cleanup() {
        for(String bg: _myTopologicallyOrdered.keySet()) {
            for(TridentProcessor p: _myTopologicallyOrdered.get(bg)) {
                p.cleanup();
            }   
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        for(Node n: _nodes) {
            declarer.declareStream(n.streamId, TridentUtils.fieldsConcat(new Fields("$batchId"), n.allOutputFields));
        }        
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
    
    
    protected static class InitialReceiver {
        List<TridentProcessor> _receivers = new ArrayList<>();
        RootFactory _factory;
        ProjectionFactory _project;
        String _stream;
        
        public InitialReceiver(String stream, Fields allFields) {
            // TODO: don't want to project for non-batch bolts...???
            // how to distinguish "batch" streams from non-batch streams?
            _stream = stream;
            _factory = new RootFactory(allFields);
            List<String> projected = new ArrayList<>(allFields.toList());
            projected.remove(0);
            _project = new ProjectionFactory(_factory, new Fields(projected));
        }
        
        public void receive(ProcessorContext context, Tuple tuple) {
            TridentTuple t = _project.create(_factory.create(tuple));
            for(TridentProcessor r: _receivers) {
                r.execute(context, _stream, t);
            }            
        }
        
        public void addReceiver(TridentProcessor p) {
            _receivers.add(p);
        }
        
        public Factory getOutputFactory() {
            return _project;
        }
    }
}
