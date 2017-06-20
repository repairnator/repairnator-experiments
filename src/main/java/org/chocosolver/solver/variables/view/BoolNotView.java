/**
 * This file is part of choco-solver, http://choco-solver.org/
 *
 * Copyright (c) 2017, IMT Atlantique. All rights reserved.
 *
 * Licensed under the BSD 4-clause license.
 * See LICENSE file in the project root for full license information.
 */
package org.chocosolver.solver.variables.view;

import org.chocosolver.solver.ICause;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.explanations.RuleStore;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Variable;
import org.chocosolver.solver.variables.delta.IIntDeltaMonitor;
import org.chocosolver.solver.variables.delta.NoDelta;
import org.chocosolver.solver.variables.events.IEventType;
import org.chocosolver.solver.variables.events.IntEventType;
import org.chocosolver.util.ESat;
import org.chocosolver.util.objects.setDataStructures.iterable.IntIterableSet;

/**
 * A view for boolean variable, that enforce not(b).
 * <br/>
 *
 * @author Charles Prud'homme
 * @since 31/07/12
 */
public final class BoolNotView extends IntView implements BoolVar {

    /**
     * Variable to observe
     */
    private final BoolVar var;

    /**
     * Create a not view based on <i>var<i/> 
     * @param var a boolean variable
     */
    public BoolNotView(BoolVar var) {
        super("not(" + var.getName() + ")", var);
        this.var = var;
    }

    @Override
    public ESat getBooleanValue() {
        return ESat.not(var.getBooleanValue());
    }

    @Override
    public boolean setToTrue(ICause cause) throws ContradictionException {
        return instantiateTo(1, cause);
    }

    @Override
    public boolean setToFalse(ICause cause) throws ContradictionException {
        return instantiateTo(0, cause);
    }

    @Override
    public boolean removeValue(int value, ICause cause) throws ContradictionException {
        return contains(value) && instantiateTo(1 - value, cause);
    }

    @Override
    public boolean removeValues(IntIterableSet values, ICause cause) throws ContradictionException {
        boolean hasChanged = false;
        if (values.contains(0)) {
            hasChanged = instantiateTo(1, cause);
        }
        if (values.contains(1)) {
            hasChanged = instantiateTo(0, cause);
        }
        return hasChanged;
    }

    @Override
    public boolean removeAllValuesBut(IntIterableSet values, ICause cause) throws ContradictionException {
        boolean hasChanged = false;
        if (!values.contains(0)) {
            hasChanged = instantiateTo(1, cause);
        }
        if (!values.contains(1)) {
            hasChanged = instantiateTo(0, cause);
        }
        return hasChanged;
    }

    @Override
    public boolean removeInterval(int from, int to, ICause cause) throws ContradictionException {
        boolean hasChanged = false;
        if (from <= to && from <= 1 && to >= 0) {
            if (from == 1) {
                hasChanged = instantiateTo(1, cause);
            } else if (to == 0) {
                hasChanged = instantiateTo(0, cause);
            } else {
                instantiateTo(2, cause);
            }
        }
        return hasChanged;
    }

    @Override
    public boolean instantiateTo(int value, ICause cause) throws ContradictionException {
        if (!this.contains(value)) {
        model.getSolver().getExplainer().instantiateTo(this, value, cause, getLB(), getUB());
            this.contradiction(cause, MSG_INST);
        }else if (!isInstantiated()){
            model.getSolver().getExplainer().instantiateTo(this, value, cause, getLB(), getUB());
            notifyPropagators(IntEventType.INSTANTIATE, cause);
            var.instantiateTo(1 - value, this);
        }
        return false;
    }

    @Override
    public boolean updateLowerBound(int value, ICause cause) throws ContradictionException {
        return value > 0 && instantiateTo(value, cause);
    }

    @Override
    public boolean updateUpperBound(int value, ICause cause) throws ContradictionException {
        return value < 1 && instantiateTo(value, cause);
    }

    @Override
    public boolean updateBounds(int lb, int ub, ICause cause) throws ContradictionException {
        boolean hasChanged = false;
        if (lb > 1) {
            var.instantiateTo(-1, cause);
        } else if (ub < 0) {
            var.instantiateTo(2, cause);
        } else {
            if (lb == 1) {
                hasChanged = instantiateTo(1, cause);
            } else if (ub == 0) {
                hasChanged = instantiateTo(0, cause);
            }
        }
        return hasChanged;
    }

    @Override
    public boolean contains(int value) {
        return var.contains(1 - value);
    }

    @Override
    public boolean isInstantiatedTo(int value) {
        return var.isInstantiatedTo(1 - value);
    }

    @Override
    public int getValue() {
        int v = var.getValue();
        return 1 - v;
    }

    @Override
    public int getLB() {
        if (var.isInstantiated()) {
            return getValue();
        } else return 0;
    }

    @Override
    public int getUB() {
        if (var.isInstantiated()) {
            return getValue();
        } else return 1;
    }

    @Override
    public int nextValue(int v) {
        if (v < 0 && contains(0)) {
            return 0;
        }
        return v <= 0 && contains(1) ? 1 : Integer.MAX_VALUE;
    }

    @Override
    public int nextValueOut(int v) {
        int lb = 0, ub = 1;
        if(var.isInstantiated()){
            lb = ub = getValue();
        }
        if (lb - 1 <= v && v <= ub) {
            return ub + 1;
        }else{
        return v + 1;
    }
    }

    @Override
    public int previousValue(int v) {
        if (v > 1 && contains(1)) {
            return 1;
        }
        return v >= 1 && contains(0) ? 0 : Integer.MIN_VALUE;
    }

    @Override
    public int previousValueOut(int v) {
        int lb = 0, ub = 1;
        if(var.isInstantiated()){
            lb = ub = getValue();
        }
        if (lb <= v && v <= ub + 1) {
            return lb - 1;
        }else{
        return v - 1;
    }
    }

    @Override
    public IIntDeltaMonitor monitorDelta(ICause propagator) {
        var.createDelta();
        if (var.getDelta() == NoDelta.singleton) {
            return IIntDeltaMonitor.Default.NONE;
        }
        return new ViewDeltaMonitor((IIntDeltaMonitor) var.monitorDelta(propagator)) {

            @Override
            protected int transform(int value) {
                return 1 - value;
            }
        };
    }

    public String toString() {
        return "not(" + var + ")";
    }

    @Override
    public BoolVar not() {
        return var;
    }

    @Override
    public void _setNot(BoolVar not) {
        assert not == var;
    }

    @Override
    public boolean hasNot() {
        return true;
    }

    @Override
    public boolean isLit() {
        return true;
    }

    @Override
    public boolean isNot() {
        return !var.isNot();
    }

    @Override
    public void setNot(boolean isNot) {
        assert isNot;
    }

    @Override
    public int getTypeAndKind() {
        return Variable.VIEW | Variable.BOOL;
    }

    @Override
    public boolean why(RuleStore ruleStore, IntVar modifiedVar, IEventType evt, int value) {
        assert modifiedVar == this.var;
        return ruleStore.addFullDomainRule(this);
    }

    @Override
    public int transformValue(int value) {
        assert value == 0 || value == 1;
        return 1- value;
    }

    @Override
    public void justifyEvent(IntVar var, ICause cause, IntEventType mask, int one, int two, int three) {
        assert mask == IntEventType.INSTANTIATE;
        model.getSolver().getExplainer().instantiateTo(this, 1 - one, var, 0, 1);
    }
}
