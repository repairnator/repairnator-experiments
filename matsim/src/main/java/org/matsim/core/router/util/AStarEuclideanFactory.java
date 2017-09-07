/* *********************************************************************** *
 * project: org.matsim.*
 * AStarLandmarksFactory
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2009 by the members listed in the COPYING,        *
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
package org.matsim.core.router.util;

import java.util.HashMap;
import java.util.Map;

import org.matsim.api.core.v01.network.Network;
import org.matsim.core.router.AStarEuclidean;

/**
 * @author dgrether
 */
public class AStarEuclideanFactory implements LeastCostPathCalculatorFactory {

	private final Map<Network, PreProcessEuclidean> preProcessData = new HashMap<>();

	@Override
	public synchronized LeastCostPathCalculator createPathCalculator(final Network network, final TravelDisutility travelCosts, final TravelTime travelTimes) {
		PreProcessEuclidean preProcessEuclidean = this.preProcessData.get(network);
		if (preProcessEuclidean == null) {
			preProcessEuclidean = new PreProcessEuclidean(travelCosts);
			preProcessEuclidean.run(network);
			this.preProcessData.put(network, preProcessEuclidean);
		}
		
		final double overdoFactor = 1.0;
		return new AStarEuclidean(network, preProcessEuclidean, travelCosts, travelTimes, overdoFactor);
	}
}