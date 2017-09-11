/* *********************************************************************** *
 * project: org.matsim.*
 * TransitDriverAgent.java
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

package org.matsim.core.mobsim.qsim.pt;

import org.matsim.core.mobsim.framework.MobsimDriverAgent;
import org.matsim.pt.transitSchedule.api.TransitStopFacility;

/**
 * Additional methods for drivers of public transport vehicles.
 *
 * @author mrieser
 */
public interface TransitDriverAgent extends MobsimDriverAgent {

	public TransitStopFacility getNextTransitStop();
	
	public double handleTransitStop(final TransitStopFacility stop, final double now);
}
