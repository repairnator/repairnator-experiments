/* *********************************************************************** *
 * project: org.matsim.*
 * InitialNode.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2013 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package org.matsim.core.router;

import org.matsim.api.core.v01.network.Node;

/**
 * Used by the MultiNodeDijkstra to store initial time and cost. 
 * 
 * @see org.matsim.core.router.MultiNodeDijkstra
 * @author cdobler
 */
public class InitialNode {
	
	public Node node;
	public final double initialCost;
	public final double initialTime;
	
	public InitialNode(final Node node, final double initialCost, final double initialTime) {
		this.node = node;
		this.initialCost = initialCost;
		this.initialTime = initialTime;
	}
	
	@Override
	public String toString() {
		return "[id=" + this.node.getId() + "]" +
				"[initialCost=" + this.initialCost + "]" +
				"[initialTime=" + this.initialTime + "]";
	}
}