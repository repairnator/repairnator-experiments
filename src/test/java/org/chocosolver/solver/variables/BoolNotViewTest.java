/**
 * This file is part of choco-solver, http://choco-solver.org/
 *
 * Copyright (c) 2017, IMT Atlantique. All rights reserved.
 *
 * Licensed under the BSD 4-clause license.
 * See LICENSE file in the project root for full license information.
 */
package org.chocosolver.solver.variables;

import org.chocosolver.solver.Model;
import org.chocosolver.util.iterators.DisposableRangeIterator;
import org.chocosolver.util.iterators.DisposableValueIterator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;

import static org.chocosolver.solver.search.strategy.Search.randomSearch;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * <br/>
 *
 * @author Charles Prud'homme
 * @since 11/02/13
 */
public class BoolNotViewTest {

    @Test(groups="1s", timeOut=60000)
    public void test1() {
        Random random = new Random();
        for (int seed = 0; seed < 2000; seed++) {
            random.setSeed(seed);
            Model ref = new Model();
            {
                BoolVar[] xs = new BoolVar[2];
                xs[0] = ref.boolVar("x");
                xs[1] = ref.boolVar("y");
                ref.sum(xs, "=", 1).post();
                ref.getSolver().setSearch(randomSearch(xs, seed));
            }
            Model model = new Model();
            {
                BoolVar[] xs = new BoolVar[2];
                xs[0] = model.boolVar("x");
                xs[1] = model.boolNotView(xs[0]);
                model.sum(xs, "=", 1).post();
                model.getSolver().setSearch(randomSearch(xs, seed));
            }
            while (ref.getSolver().solve()) ;
            while (model.getSolver().solve()) ;
            assertEquals(model.getSolver().getSolutionCount(), ref.getSolver().getSolutionCount());

        }
    }

    @Test(groups="1s", timeOut=60000)
    public void testIt() {
        Model ref = new Model();
        BoolVar o = ref.boolVar("b");
        BoolVar v = ref.boolNotView(o);
        DisposableValueIterator vit = v.getValueIterator(true);
        while (vit.hasNext()) {
            Assert.assertTrue(o.contains(vit.next()));
        }
        vit.dispose();
        vit = v.getValueIterator(false);
        while (vit.hasNext()) {
            Assert.assertTrue(o.contains(vit.next()));
        }
        vit.dispose();
        DisposableRangeIterator rit = v.getRangeIterator(true);
        while (rit.hasNext()) {
            rit.next();
            Assert.assertTrue(o.contains(rit.min()));
            Assert.assertTrue(o.contains(rit.max()));
        }
        rit = v.getRangeIterator(false);
        while (rit.hasNext()) {
            rit.next();
            Assert.assertTrue(o.contains(rit.min()));
            Assert.assertTrue(o.contains(rit.max()));
        }
    }

    @Test(groups="1s", timeOut=60000)
    public void testPrevNext() {
        Model model = new Model();
        BoolVar a = model.boolVar("a");
        BoolVar b = model.boolVar("b");
        model.arithm(a, "+", model.boolNotView(b), "=", 2).post();
        assertTrue(model.getSolver().solve());
    }
}
