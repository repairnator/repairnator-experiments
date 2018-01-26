/**
 * This file is part of choco-solver, http://choco-solver.org/
 *
 * Copyright (c) 2017, IMT Atlantique. All rights reserved.
 *
 * Licensed under the BSD 4-clause license.
 * See LICENSE file in the project root for full license information.
 */
package org.chocosolver.solver.constraints.nary.min_max;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.util.ESat;
import org.chocosolver.util.tools.ArrayUtils;

/**
 * <br/>
 *
 * @author Jean-Guillaume Fages
 * @since 15/12/2013
 */
public class PropBoolMax extends Propagator<BoolVar> {

    private final int n;
    private int x1, x2;

    public PropBoolMax(BoolVar[] variables, BoolVar maxVar) {
        super(ArrayUtils.append(variables, new BoolVar[]{maxVar}), PropagatorPriority.UNARY, true);
        n = variables.length;
        x1 = -1;
        x2 = -1;
        assert n > 0;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        x1 = 1;
        x2 = 0;
        int c = 2;
        for (int i = 0; i < n; i++) {
            if (c>0 && !vars[i].isInstantiated()) {
                if (c == 2 && i > 1) {
                    x2 = x1;
                    x1 = i;
                } else if(i > 1){
                    x2 = i;
                }
                c--;
            } else if (vars[i].isInstantiatedTo(1)) {
                vars[n].instantiateTo(1, this);
                if (vars[n].isInstantiatedTo(1)) {
                    setPassive();
                    return;
                }
            }
        }
        filter();
    }

    public void propagate(int idxVarInProp, int mask) throws ContradictionException {
        if (idxVarInProp == n) {
            filter();
        } else {
            if (vars[idxVarInProp].isInstantiatedTo(1)) {
                vars[n].instantiateTo(1, this);
                if (vars[n].isInstantiatedTo(1)) {
                    setPassive();
                }
            } else if (idxVarInProp == x1 || idxVarInProp == x2) {
                if (idxVarInProp == x1) {
                    int t = x1;
                    x1 = x2;
                    x2 = t;
                }
                for (int i = 0; i < n; i++) {
                    if (i != x1 && !vars[i].isInstantiated()) {
                        x2 = i;
                        break;
                    }else if (vars[i].isInstantiatedTo(1)) {
                        vars[n].instantiateTo(1, this);
                        if (vars[n].isInstantiatedTo(1)) {
                            setPassive();
                            return;
                        }
                    }
                }
                filter();
            }
        }
    }

    public void filter() throws ContradictionException {
        int b1 = vars[x1].isInstantiated()? vars[x1].getValue():2;
        int b2 = vars[x2].isInstantiated()? vars[x2].getValue():2;
        int bn = vars[n].isInstantiated()? vars[n].getValue():2;

        if(b1 == 0 && b2 == 0){
            setPassive();
            vars[n].instantiateTo(0, this);
        }else if(bn == 1){
            if(b1 == 0){
                vars[x2].instantiateTo(1, this);
            }else if(b2 == 0){
                vars[x1].instantiateTo(1, this);
            }
        }else if(bn == 0) {
            for (int i = 0; i < n; i++) {
                vars[i].instantiateTo(0, this);
            }
        }
    }

    @Override
    public ESat isEntailed() {
        int ub = vars[n].getUB();
        for (int i = 0; i < n; i++) {
            if (vars[i].getLB() > ub) {
                return ESat.FALSE;
            }
        }
        for (int i = 0; i < n; i++) {
            if (vars[i].getUB() > ub) {
                return ESat.UNDEFINED;
            }
        }
        if (vars[n].isInstantiated()) {
            for (int i = 0; i < n; i++) {
                if (vars[i].isInstantiatedTo(ub)) {
                    return ESat.TRUE;
                }
            }
        }
        return ESat.UNDEFINED;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        sb.append(vars[n]).append(" = max(");
        sb.append(vars[0]);
        for (int i = 1; i < n; i++) {
            sb.append(", ");
            sb.append(vars[i]);
        }
        sb.append(")");
        return sb.toString();

    }
}
