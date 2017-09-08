/**
 * This file is part of choco-solver, http://choco-solver.org/
 *
 * Copyright (c) 2017, IMT Atlantique. All rights reserved.
 *
 * Licensed under the BSD 4-clause license.
 * See LICENSE file in the project root for full license information.
 */
package org.chocosolver.solver.constraints.nary.cumulative;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.objects.setDataStructures.ISet;



/**
 * Class able to filter a subset of tasks for the cumulative constraint
 * @author Jean-Guillaume Fages
 */
public abstract class CumulFilter {

	//***********************************************************************************
	// VARIABLES
	//***********************************************************************************

	protected Propagator<IntVar> aCause;
	protected int nbMaxTasks;

	//***********************************************************************************
	// CONSTRUCTORS
	//***********************************************************************************

	/**
	 * An object which can filter subset of tasks for the cumulative constraint
	 * @param nbMaxTasks	maximum number of tasks
	 * @param cause			a cumulative propagator
	 */
	public CumulFilter(int nbMaxTasks, Propagator<IntVar> cause){
		this.nbMaxTasks = nbMaxTasks;
		this.aCause = cause;
	}

	//***********************************************************************************
	// METHODS
	//***********************************************************************************

	/**
	 * Filters the cumulative constraint over the subset of tasks induced by tasks
	 * @param s		start variables
	 * @param d		duration variables
	 * @param e		end variables
	 * @param h		height variables
	 * @param capa	maximum capacity variable
	 * @param tasks	subset of tasks to filter
	 * @throws ContradictionException
	 */
	public abstract void filter(IntVar[] s, IntVar[] d, IntVar[] e, IntVar[] h, IntVar capa, ISet tasks) throws ContradictionException;
}
