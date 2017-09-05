/* *********************************************************************** *
 * project: org.matsim.*
 * EdgeLengthMeanTask.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2011 by the members listed in the COPYING,        *
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
package org.matsim.contrib.socnetgen.sna.graph.spatial.analysis;

/**
 * @author illenberger
 *
 */
public class EdgeLengthMeanTask extends EdgeLengthSumTask {

	public EdgeLengthMeanTask() {
		setModule(new EdgeLengthMean());
		setKey("d_mean");
	}

	public EdgeLengthMeanTask(EdgeLengthMean module) {
		setModule(module);
		setKey("d_mean");
	}

}
