/**
 * This file is part of choco-solver, http://choco-solver.org/
 *
 * Copyright (c) 2017, IMT Atlantique. All rights reserved.
 *
 * Licensed under the BSD 4-clause license.
 * See LICENSE file in the project root for full license information.
 */
package org.chocosolver.solver.constraints.nary.alldifferent.algo;

import gnu.trove.map.hash.TIntIntHashMap;
import org.chocosolver.solver.ICause;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.graphOperations.connectivity.StrongConnectivityFinder;
import org.chocosolver.util.objects.graphs.DirectedGraph;
import org.chocosolver.util.objects.setDataStructures.ISetIterator;
import org.chocosolver.util.objects.setDataStructures.SetType;

import java.util.BitSet;

/**
 * Algorithm of Alldifferent with AC
 *
 * Uses Regin algorithm
 * Runs in O(m.n) worst case time for the initial propagation
 * but has a good average behavior in practice
 * <p/>
 * Keeps track of previous matching for further calls
 * <p/>
 * 
 * @author Jean-Guillaume Fages
 */
public class AlgoAllDiffAC {

    //***********************************************************************************
    // VARIABLES
    //***********************************************************************************

    private int n, n2;
    private DirectedGraph digraph;
    private int[] matching;
    private int[] nodeSCC;
    private BitSet free;
    private StrongConnectivityFinder SCCfinder;
    // for augmenting matching (BFS)
    private int[] father;
    private BitSet in;
    private TIntIntHashMap map;
    private int[] fifo;
    private IntVar[] vars;
    private ICause aCause;

    //***********************************************************************************
    // CONSTRUCTORS
    //***********************************************************************************

    public AlgoAllDiffAC(IntVar[] variables, ICause cause) {
        this.vars = variables;
		aCause = cause;
        n = vars.length;
        matching = new int[n];
        for (int i = 0; i < n; i++) {
            matching[i] = -1;
        }
        map = new TIntIntHashMap();
        IntVar v;
        int ub;
        int idx = n;
        for (int i = 0; i < n; i++) {
            v = vars[i];
            ub = v.getUB();
            for (int j = v.getLB(); j <= ub; j = v.nextValue(j)) {
                if (!map.containsKey(j)) {
                    map.put(j, idx);
                    idx++;
                }
            }
        }
        n2 = idx;
        fifo = new int[n2];
        digraph = new DirectedGraph(n2 + 1, SetType.BITSET, false);
        free = new BitSet(n2);
        father = new int[n2];
        in = new BitSet(n2);
        SCCfinder = new StrongConnectivityFinder(digraph);
    }

    //***********************************************************************************
    // PROPAGATION
    //***********************************************************************************

    public boolean propagate() throws ContradictionException {
        findMaximumMatching();
        return filter();
    }

    //***********************************************************************************
    // Initialization
    //***********************************************************************************

    private void findMaximumMatching() throws ContradictionException {
        for (int i = 0; i < n2; i++) {
            digraph.getSuccOf(i).clear();
            digraph.getPredOf(i).clear();
        }
        free.set(0, n2);
        int k, ub;
        IntVar v;
        for (int i = 0; i < n; i++) {
            v = vars[i];
            ub = v.getUB();
            int mate = matching[i];
            for (k = v.getLB(); k <= ub; k = v.nextValue(k)) {
                int j = map.get(k);
                if (mate == j) {
                    assert free.get(i) && free.get(j);
                    digraph.addArc(j, i);
                    free.clear(i);
                    free.clear(j);
                } else {
                    digraph.addArc(i, j);
                }
            }
        }
        for (int i = free.nextSetBit(0); i >= 0 && i < n; i = free.nextSetBit(i + 1)) {
            tryToMatch(i);
        }
        for (int i = 0; i < n; i++) {
            matching[i] = digraph.getPredOf(i).isEmpty()?-1:digraph.getPredOf(i).iterator().next();
        }
    }

    private void tryToMatch(int i) throws ContradictionException {
        int mate = augmentPath_BFS(i);
        if (mate != -1) {
            free.clear(mate);
            free.clear(i);
            int tmp = mate;
            while (tmp != i) {
                digraph.removeArc(father[tmp], tmp);
                digraph.addArc(tmp, father[tmp]);
                tmp = father[tmp];
            }
        } else {
            vars[0].instantiateTo(vars[0].getLB()-1,aCause);
        }
    }

    private int augmentPath_BFS(int root) {
        in.clear();
        int indexFirst = 0, indexLast = 0;
        fifo[indexLast++] = root;
        int x;
        ISetIterator succs;
        while (indexFirst != indexLast) {
            x = fifo[indexFirst++];
            succs = digraph.getSuccOf(x).iterator();
            while (succs.hasNext()) {
                int y = succs.nextInt();
                if (!in.get(y)) {
                    father[y] = x;
                    fifo[indexLast++] = y;
                    in.set(y);
                    if (free.get(y)) {
                        return y;
                    }
                }
            }
        }
        return -1;
    }

    //***********************************************************************************
    // PRUNING
    //***********************************************************************************

    private void buildSCC() {
        if (n2 > n * 2) {
            digraph.removeNode(n2);
            digraph.addNode(n2);
            for (int i = n; i < n2; i++) {
                if (free.get(i)) {
                    digraph.addArc(i, n2);
                } else {
                    digraph.addArc(n2, i);
                }
            }
        }
        SCCfinder.findAllSCC();
        nodeSCC = SCCfinder.getNodesSCC();
        digraph.removeNode(n2);
    }

    private boolean filter() throws ContradictionException {
        boolean filter =false;
        buildSCC();
        int j, ub;
        IntVar v;
        for (int i = 0; i < n; i++) {
            v = vars[i];
            ub = v.getUB();
            for (int k = v.getLB(); k <= ub; k = v.nextValue(k)) {
                j = map.get(k);
                if (nodeSCC[i] != nodeSCC[j]) {
                    if (matching[i] == j) {
                        filter |= v.instantiateTo(k, aCause);
                    } else {
                        filter |= v.removeValue(k, aCause);
                        digraph.removeArc(i, j);
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            v = vars[i];
            if (!v.hasEnumeratedDomain()) {
                ub = v.getUB();
                for (int k = v.getLB(); k <= ub; k++) {
                    j = map.get(k);
                    if (!(digraph.arcExists(i, j) || digraph.arcExists(j, i))) {
                        filter |= v.removeValue(k, aCause);
                    }
                }
                int lb = v.getLB();
                for (int k = v.getUB(); k >= lb; k--) {
                    j = map.get(k);
                    if (!(digraph.arcExists(i, j) || digraph.arcExists(j, i))) {
                        filter |= v.removeValue(k, aCause);
                    }
                }
            }
        }
        return filter;
    }
}
