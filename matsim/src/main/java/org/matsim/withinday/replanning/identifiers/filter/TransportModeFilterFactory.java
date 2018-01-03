/* *********************************************************************** *
 * project: org.matsim.*
 * TransportModeFilterFactory.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2012 by the members listed in the COPYING,        *
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

package org.matsim.withinday.replanning.identifiers.filter;

import java.util.Set;

import org.matsim.withinday.mobsim.MobsimDataProvider;
import org.matsim.withinday.replanning.identifiers.interfaces.AgentFilterFactory;

public class TransportModeFilterFactory implements AgentFilterFactory {

	private final Set<String> modes;
	private final MobsimDataProvider mobsimDataProvider;
	
	public TransportModeFilterFactory(Set<String> modes, MobsimDataProvider mobsimDataProvider) {
		this.modes = modes;
		this.mobsimDataProvider = mobsimDataProvider;
	}
	
	@Override
	public TransportModeFilter createAgentFilter() {
		return new TransportModeFilter(this.mobsimDataProvider.getAgents(), this.modes);
	}

}