/* *********************************************************************** *
 * project: org.matsim.*
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

package org.matsim.pt;

import java.util.List;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Identifiable;
import org.matsim.pt.transitSchedule.api.TransitLine;
import org.matsim.vehicles.Vehicle;


public interface Umlauf extends Identifiable<Umlauf> {

	List<UmlaufStueckI> getUmlaufStuecke();

	public Id<Vehicle> getVehicleId();

	public void setVehicleId(Id<Vehicle> vehicleId);
	/**
	 * @deprecated as a Umlauf can contain UmlaufStueckI from several Lines. Use getId() instead. dg, nov 2012
	 */
	@Deprecated
	Id<TransitLine> getLineId();

}
