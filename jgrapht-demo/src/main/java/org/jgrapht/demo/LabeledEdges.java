/*
 * (C) Copyright 2012-2018, by Barak Naveh and Contributors.
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

package org.jgrapht.demo;

import org.jgrapht.*;
import org.jgrapht.graph.*;

import java.util.*;

/**
 * An example of how to apply edge labels using a custom edge class.
 *
 * @author Barak Naveh
 * @since Jul 27, 2003
 */
public class LabeledEdges
{
    private static final String FRIEND = "friend";
    private static final String ENEMY = "enemy";

    /**
     * The starting point for the demo.
     *
     * @param args ignored.
     */
    public static void main(String[] args)
    {
        //@example:create:begin
        Graph<String, RelationshipEdge> graph = new DefaultDirectedGraph<>(RelationshipEdge.class);

        ArrayList<String> people = new ArrayList<String>();
        people.add("John");
        people.add("James");
        people.add("Sarah");
        people.add("Jessica");

        // John is everyone's friend
        for (String person : people) {
            graph.addVertex(person);
            if (!person.equals("John")) {
                graph.addEdge(
                    "John", person,
                    new RelationshipEdge(FRIEND));
            }
        }

        // Apparently James doesn't really like John
        graph.addEdge("James", "John", new RelationshipEdge(ENEMY));

        // Jessica is Sarah and James's friend
        graph.addEdge("Jessica", "Sarah", new RelationshipEdge(FRIEND));
        graph.addEdge("Jessica", "James", new RelationshipEdge(FRIEND));

        // But Sarah doesn't really like James
        graph.addEdge("Sarah", "James", new RelationshipEdge(ENEMY));
        //@example:create:end

        //@example:print:begin
        for (RelationshipEdge edge : graph.edgeSet()) {
            String v1 = graph.getEdgeSource(edge);
            String v2 = graph.getEdgeTarget(edge);
            if (edge.getLabel().equals("enemy")) {
                System.out.printf(v1 + " is an enemy of " + v2 + "\n");
            } else if (edge.getLabel().equals("friend")) {
                System.out.printf(v1 + " is a friend of " + v2 + "\n");
            }
        }
        //@example:print:end

        assert(isEnemyOf(graph, "James", "John"));
    }

    //@example:isEnemyOf:begin
    private static boolean isEnemyOf(
        Graph<String, RelationshipEdge> graph,
        String person1,
        String person2)
    {
        return graph.getEdge(person1, person2).getLabel().equals(ENEMY);
    }
    //@example:isEnemyOf:end
}

/**
 * Custom edge class labeled with relationship type.
 */
//@example:edgeclass:begin
class RelationshipEdge extends DefaultEdge
{
    private String label;

    /**
     * Constructs a relationship edge
     *
     * @param label the label of the new edge.
     * 
     */
    public RelationshipEdge(String label)
    {
        this.label = label;
    }

    /**
     * Gets the label associated with this edge.
     *
     * @return edge label
     */
    public String getLabel()
    {
        return label;
    }

    @Override
    public String toString()
    {
        return "(" + getSource() + " : " + getTarget() + " : " + label + ")";
    }
}
//@example:edgeclass:end
