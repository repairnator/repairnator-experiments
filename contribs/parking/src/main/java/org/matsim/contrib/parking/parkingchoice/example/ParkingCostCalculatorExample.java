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
package org.matsim.contrib.parking.parkingchoice.example;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.parking.parkingchoice.PC2.scoring.ParkingCostModel;

/**
 * @author jbischoff
 *
 */
public class ParkingCostCalculatorExample implements ParkingCostModel {

	private double hourlyParkingCharge;

	public ParkingCostCalculatorExample(double hourlyParkingCharge) {
		this.hourlyParkingCharge = hourlyParkingCharge;
	}
	
	@Override
	public double calcParkingCost(double arrivalTimeInSeconds, double durationInSeconds, Id<Person> personId, Id parkingFacilityId) {
					
			return hourlyParkingCharge*(durationInSeconds/3600);
		
	}

}
