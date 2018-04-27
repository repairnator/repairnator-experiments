/*
 *  Licensed to GraphHopper GmbH under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper GmbH licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.routing;

import com.graphhopper.routing.ch.CHEntry;
import com.graphhopper.routing.ch.Path4CH;
import com.graphhopper.routing.util.DefaultEdgeFilter;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.routing.util.TraversalMode;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.SPTEntry;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.GHUtility;

import java.util.Locale;

public class DijkstraBidirectionEdgeCHNoSOD extends AbstractBidirectionEdgeCHNoSOD {
    public DijkstraBidirectionEdgeCHNoSOD(Graph graph, Weighting weighting, TraversalMode traversalMode) {
        super(graph, weighting, traversalMode);
    }

    @Override
    protected CHEntry createStartEntry(int node, double weight, boolean reverse) {
        return new CHEntry(node, weight);
    }

    @Override
    protected CHEntry createEntry(EdgeIteratorState edge, int edgeId, double weight, SPTEntry parent, boolean reverse) {
        CHEntry entry = new CHEntry(edge.getEdge(), edgeId, edge.getAdjNode(), weight);
        entry.parent = parent;
        return entry;
    }

    @Override
    protected void updateEntry(SPTEntry entry, EdgeIteratorState edge, int edgeId, double weight, SPTEntry parent, boolean reverse) {
        entry.edge = edge.getEdge();
        ((CHEntry) entry).incEdge = edgeId;
        entry.weight = weight;
        entry.parent = parent;
    }

    @Override
    public String getName() {
        return "dijkstrabi|ch|edge_based|no_sod";
    }

}
