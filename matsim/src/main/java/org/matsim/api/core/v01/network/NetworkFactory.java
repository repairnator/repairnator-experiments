/* *********************************************************************** *
 * project: org.matsim.*
 * NetworkBuilder
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2009, 2011 by the members listed in the COPYING,  *
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

package org.matsim.api.core.v01.network;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.core.api.internal.MatsimFactory;
import org.matsim.core.network.LinkFactory;

/**
 * @author dgrether
 * @author mrieser
 */
public interface NetworkFactory extends MatsimFactory {

	public Node createNode(final Id<Node> id, final Coord coord);
	// this is NOT CoordI, since it wants to rely on the fact that coord is immutable! kai, jul'16

	/**
	 * Creates a link with the given id leading from one node to another.
	 *
	 * @param id
	 * @param fromNode
	 * @param toNode
	 * @return the newly created link
	 */
	public Link createLink(final Id<Link> id, final Node fromNode, final Node toNode);

	void setLinkFactory(LinkFactory factory);
	// yyyyyy I think that this method should not be here, since it allows to change the factory during the run.  Should be set at 
	// construction.  Would best be done by guice, but if one loads the scenario before instantiating the controler, we don't 
	// have that.  kai, may'17

}
