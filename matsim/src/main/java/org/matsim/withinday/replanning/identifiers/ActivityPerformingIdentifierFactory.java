/* *********************************************************************** *
 * project: org.matsim.*
 * ActivityPerformingIdentifierFactory.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2010 by the members listed in the COPYING,        *
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

package org.matsim.withinday.replanning.identifiers;

import org.matsim.withinday.mobsim.MobsimDataProvider;
import org.matsim.withinday.replanning.identifiers.interfaces.DuringActivityAgentSelector;
import org.matsim.withinday.replanning.identifiers.interfaces.DuringActivityIdentifierFactory;
import org.matsim.withinday.replanning.identifiers.tools.ActivityReplanningMap;

public class ActivityPerformingIdentifierFactory extends DuringActivityIdentifierFactory {

	private ActivityReplanningMap activityReplanningMap;
	private MobsimDataProvider mobsimDataProvider;
	
	public ActivityPerformingIdentifierFactory(ActivityReplanningMap activityReplanningMap, MobsimDataProvider mobsimDataProvider) {
		this.activityReplanningMap = activityReplanningMap;
		this.mobsimDataProvider = mobsimDataProvider;
	}
	
	@Override
	public DuringActivityAgentSelector createIdentifier() {
		DuringActivityAgentSelector identifier = new ActivityPerformingIdentifier(this.activityReplanningMap, this.mobsimDataProvider);
		this.addAgentFiltersToIdentifier(identifier);
		identifier.setAgentSelectorFactory(this);
		return identifier;
	}

}
