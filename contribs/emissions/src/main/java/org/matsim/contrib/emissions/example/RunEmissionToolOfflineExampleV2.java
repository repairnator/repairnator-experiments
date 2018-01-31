/* *********************************************************************** *
 * project: org.matsim.*
 * RunEmissionToolOffline.java
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
package org.matsim.contrib.emissions.example;

import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.emissions.EmissionModule;
import org.matsim.contrib.emissions.utils.EmissionsConfigGroup;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigReader;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.events.algorithms.EventWriterXML;
import org.matsim.core.scenario.ScenarioUtils;


/**
 * 
 * Use the config file as created by the 
 * {@link CreateEmissionConfig CreateEmissionConfig} to calculate
 * emissions based on the link leave events of an events file. Resulting emission events are written into an event file.
 *
 * @author benjamin, julia
 */
public class RunEmissionToolOfflineExampleV2 {
	
	private final static String runDirectory = "./test/output/";
	private static final String configFile = "./test/input/org/matsim/contrib/emissions/config_v2.xml";
	private final static Integer lastIteration = getLastIteration();
	
	private static final String eventsPath = runDirectory + "ITERS/it." + lastIteration + "/" +  lastIteration;
	private static final String eventsFile = eventsPath + ".events.xml.gz";
	private static final String emissionEventOutputFile = eventsPath + ".emission.events.offline.xml.gz";
	
	// =======================================================================================================		
	
	public static void main (String[] args) throws Exception{
		Config config = ConfigUtils.loadConfig(configFile, new EmissionsConfigGroup());

		Scenario scenario = ScenarioUtils.loadScenario(config);

		EventsManager eventsManager = EventsUtils.createEventsManager();
		EmissionModule emissionModule = new EmissionModule(scenario, eventsManager);

		EventWriterXML emissionEventWriter = new EventWriterXML(emissionEventOutputFile);
		emissionModule.getEmissionEventsManager().addHandler(emissionEventWriter);

		MatsimEventsReader matsimEventsReader = new MatsimEventsReader(eventsManager);
		matsimEventsReader.readFile(eventsFile);
		
		emissionEventWriter.closeFile();

		emissionModule.writeEmissionInformation();
	}

	private static int getLastIteration() {
		Config config = new Config();
		config.addCoreModules();
		ConfigReader configReader = new ConfigReader(config);
		configReader.readFile(RunEmissionToolOfflineExampleV2.configFile);
        return config.controler().getLastIteration();
	}
}