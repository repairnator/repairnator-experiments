/*
 * (C) Copyright 2018-2018, by Joris Kinable and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * This program and the accompanying materials are dual-licensed under
 * either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation, or (at your option) any
 * later version.
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation.
 */
package org.jgrapht.alg.interfaces;

import org.jgrapht.util.*;

import java.util.*;

/**
 * Computes a (weighted) <a href="http://mathworld.wolfram.com/VertexCover.html">vertex cover</a> in
 * an undirected graph. A vertex cover of a graph is a set of vertices such that each edge of the
 * graph is incident to at least one vertex in the set. A minimum vertex cover is a vertex cover
 * having the smallest possible number of vertices for a given graph. The size of a minimum vertex
 * cover of a graph $G$ is known as the vertex cover number. A vertex cover of minimum weight is a
 * vertex cover where the sum of weights assigned to the individual vertices in the cover has been
 * minimized. The minimum vertex cover problem is a special case of the minimum weighted vertex
 * cover problem where all vertices have equal weight. Consequently, any algorithm designed for the
 * weighted version of the problem can also solve instances of the unweighted version.
 *
 * @param <V> vertex type
 *
 * @author Joris Kinable
 */
public interface VertexCoverAlgorithm<V>
{

    /**
     * Computes a vertex cover.
     *
     * @return a vertex cover
     */
    VertexCover<V> getVertexCover();

    /**
     * A <a href="http://mathworld.wolfram.com/VertexCover.html">Vertex Cover</a>
     *
     * @param <V> the vertex type
     */
    interface VertexCover<V>
        extends
        Set<V>
    {

        /**
         * Returns the weight of the vertex cover. When solving a weighted vertex cover problem, the
         * weight returned is the sum of the weights of the vertices in the vertex cover. When
         * solving the unweighted variant, the cardinality of the vertex cover is returned instead.
         *
         * @return weight of the independent set
         */
        double getWeight();
    }

    /**
     * Default implementation of a (weighted) vertex cover
     *
     * @param <V> the vertex type
     */
    class VertexCoverImpl<V>
        extends
        WeightedUnmodifiableSet<V>
        implements
        VertexCover<V>
    {

        private static final long serialVersionUID = 3922451519162460179L;

        public VertexCoverImpl(Set<V> vertexCover)
        {
            super(vertexCover);
        }

        public VertexCoverImpl(Set<V> vertexCover, double weight)
        {
            super(vertexCover, weight);
        }
    }
}
