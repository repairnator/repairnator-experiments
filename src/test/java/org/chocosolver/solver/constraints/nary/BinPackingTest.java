/**
 * This file is part of choco-solver, http://choco-solver.org/
 *
 * Copyright (c) 2017, IMT Atlantique. All rights reserved.
 *
 * Licensed under the BSD 4-clause license.
 * See LICENSE file in the project root for full license information.
 */
package org.chocosolver.solver.constraints.nary;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author Jean-Guillaume Fages
 */
public class BinPackingTest {

	@DataProvider(name = "params")
	public Object[][] data1D(){
		// indicates whether to use explanations or not
		List<Object[]> elt = new ArrayList<>();
		elt.add(new Object[]{true});
		elt.add(new Object[]{false});
		return elt.toArray(new Object[elt.size()][1]);
	}

	@Test(groups="1s", timeOut=60000)
	public void testFixedLoadBackPropag() {
		Model model = new Model();
		int[] itemSize = new int[]{2,3,1};
		IntVar[] itemBin = model.intVarArray("binOfItem",3,-1,1);
		IntVar[] binLoad = model.intVarArray("binLoad",2,3,3);
		model.binPacking(itemBin,itemSize,binLoad,0).post();
		model.getSolver().showSolutions();
		model.getSolver().setSearch(Search.inputOrderLBSearch(itemBin));
		boolean sol = model.getSolver().solve();
		assertTrue(sol);
		assertEquals(0, model.getSolver().getFailCount());
		assertEquals(2, model.getSolver().getNodeCount());
	}

	@Test(groups="1s", timeOut=60000, dataProvider = "params")
	public void test2(boolean decomp) {
		Model model = new Model();
		int[] itemSize = new int[]{2,3,1};
		IntVar[] itemBin = model.intVarArray(3,-1,1);
		IntVar[] binLoad = model.intVarArray(3,-5,5);
		int offset = -1;
		if(decomp){
			bpDecomposition(itemBin,itemSize,binLoad,offset).post();
		}else{
			model.binPacking(itemBin,itemSize,binLoad,offset).post();
		}
		while(model.getSolver().solve()){
			assertTrue(itemBin[0].getValue()>=offset);
			assertTrue(itemBin[1].getValue()>=offset);
			assertTrue(binLoad[0].getValue()>=0);
			assertTrue(binLoad[1].getValue()>=0);
			assertTrue(binLoad[2].getValue()>=0);
		}
		assertEquals(24, model.getSolver().getSolutionCount());
	}

	@Test(groups="1s", timeOut=60000, dataProvider = "params")
	public void test3(boolean decomp) {
		Model model = new Model();
		int[] itemSize = new int[]{2,3,1};
		IntVar[] itemBin = model.intVarArray(3,-1,1);
		IntVar[] binLoad = model.intVarArray(3,-5,5);
		int offset = 1;
		if(decomp){
			bpDecomposition(itemBin,itemSize,binLoad,offset).post();
		}else{
			model.binPacking(itemBin,itemSize,binLoad,offset).post();
		}
		System.out.println(model.getSolver().isSatisfied());
		model.getSolver().solve();
		assertEquals(0, model.getSolver().getSolutionCount());
	}

	private static Constraint bpDecomposition(IntVar[] itemBin, int[] itemSize, IntVar[] binLoad, int offset){
		int nbBins = binLoad.length;
		int nbItems = itemBin.length;
		Model s = itemBin[0].getModel();
		BoolVar[][] xbi = s.boolVarMatrix("xbi", nbBins, nbItems);
		int sum = 0;
		for (int is : itemSize) {
			sum += is;
		}
		// constraints
		Constraint[] bpcons = new Constraint[nbItems + nbBins + 1];
		for (int i = 0; i < nbItems; i++) {
			bpcons[i] = s.boolsIntChanneling(ArrayUtils.getColumn(xbi, i), itemBin[i], offset);
		}
		for (int b = 0; b < nbBins; b++) {
			bpcons[nbItems + b] = s.scalar(xbi[b], itemSize, "=", binLoad[b]);
		}
		bpcons[nbItems + nbBins] = s.sum(binLoad, "=", sum);
		return Constraint.merge("BinPacking",bpcons);
	}
}
