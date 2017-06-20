/**
 * This file is part of choco-solver, http://choco-solver.org/
 *
 * Copyright (c) 2017, IMT Atlantique. All rights reserved.
 *
 * Licensed under the BSD 4-clause license.
 * See LICENSE file in the project root for full license information.
 */
package org.chocosolver.solver.variables.impl;

import org.chocosolver.memory.structure.BasicIndexedBipartiteSet;
import org.chocosolver.solver.ICause;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.delta.IEnumDelta;
import org.chocosolver.solver.variables.delta.IIntDeltaMonitor;
import org.chocosolver.solver.variables.delta.NoDelta;
import org.chocosolver.solver.variables.delta.OneValueDelta;
import org.chocosolver.solver.variables.delta.monitor.OneValueDeltaMonitor;
import org.chocosolver.solver.variables.events.IEventType;
import org.chocosolver.solver.variables.events.IntEventType;
import org.chocosolver.util.ESat;
import org.chocosolver.util.iterators.*;
import org.chocosolver.util.objects.setDataStructures.iterable.IntIterableSet;

import java.util.Iterator;

/**
 * <br/>
 *
 * @author Charles Prud'homme
 * @since 18 nov. 2010
 */
public class BoolVarImpl extends AbstractVariable implements BoolVar {

    /**
     * The offset, that is the minimal value of the domain (stored at index 0).
     * Thus the entry at index i corresponds to x=i+offset).
     */
    private final int offset;

    /**
     * indicate the value of the domain : false = 0, true = 1
     */
    private int mValue;

    /**
     * A bi partite set indicating for each value whether it is present or not.
     * If the set contains the domain, the variable is not instanciated.
     */
    private final BasicIndexedBipartiteSet notInstanciated;
    /**
     * To iterate over removed values
     */
    private IEnumDelta delta = NoDelta.singleton;
    /**
     * To iterate over values in the domain
     */
    private DisposableValueIterator _viterator;
    /**
     * To iterate over ranges
     */
    private DisposableRangeIterator _riterator;
    /**
     * Value iterator allowing for(int i:this) loops
     */
    private IntVarValueIterator _javaIterator;
    /**
     * Set to <tt>true</tt> if this variable reacts is associated with at least one propagator which reacts
     * on value removal
     */
    private boolean reactOnRemoval = false;

    /**
     * Associate boolean variable expressing not(this)
     */
    private BoolVar not;
    /**
     * For boolean expression purpose
     */
    private boolean isNot = false;

    /**
     * Create a BoolVar {0,1} or {true, false}
     * @param name name of the variable
     * @param model declaring model
     */
    public BoolVarImpl(String name, Model model) {
        super(name, model);
        notInstanciated = this.model.getEnvironment().getSharedBipartiteSetForBooleanVars();
        this.offset = notInstanciated.add();
        mValue = 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Removes {@code value}from the domain of {@code this}. The instruction comes from {@code propagator}.
     * <ul>
     * <li>If {@code value} is out of the domain, nothing is done and the return value is {@code false},</li>
     * <li>if removing {@code value} leads to a dead-end (domain wipe-out),
     * a {@code ContradictionException} is thrown,</li>
     * <li>otherwise, if removing {@code value} from the domain can be done safely,
     * the event type is created (the original event can be promoted) and observers are notified
     * and the return value is {@code true}</li>
     * </ul>
     *
     * @param value value to remove from the domain (int)
     * @param cause removal releaser
     * @return true if the value has been removed, false otherwise
     * @throws ContradictionException if the domain become empty due to this action
     */
    @Override
    public boolean removeValue(int value, ICause cause) throws ContradictionException {
        assert cause != null;
        if (value == 0)
            return instantiateTo(1, cause);
        else if (value == 1)
            return instantiateTo(0, cause);
        return false;
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
                hasChanged = instantiateTo(0, cause);
            } else if (to == 0) {
                hasChanged = instantiateTo(1, cause);
            } else {
                model.getSolver().getExplainer().instantiateTo(this, 2, cause, 0, 1);
                this.contradiction(cause, MSG_UNKNOWN);

            }
        }
        return hasChanged;
    }

    /**
     * Instantiates the domain of {@code this} to {@code value}. The instruction comes from {@code propagator}.
     * <ul>
     * <li>If the domain of {@code this} is already instantiated to {@code value},
     * nothing is done and the return value is {@code false},</li>
     * <li>If the domain of {@code this} is already instantiated to another value,
     * then a {@code ContradictionException} is thrown,</li>
     * <li>Otherwise, the domain of {@code this} is restricted to {@code value} and the observers are notified
     * and the return value is {@code true}.</li>
     * </ul>
     *
     * @param value instantiation value (int)
     * @param cause instantiation releaser
     * @return true if the instantiation is done, false otherwise
     * @throws ContradictionException if the domain become empty due to this action
     */
    @Override
    public boolean instantiateTo(int value, ICause cause) throws ContradictionException {
        // BEWARE: THIS CODE SHOULD NOT BE MOVED TO THE DOMAIN TO NOT DECREASE PERFORMANCES!
        assert cause != null;
        boolean inst = !notInstanciated.contains(offset);
        if ((inst && mValue != value) || (value < 0 || value > 1)){
            model.getSolver().getExplainer().instantiateTo(this, value, cause, getLB(), getUB());
            this.contradiction(cause, MSG_INST);
        } else if (!inst){
            IntEventType e = IntEventType.INSTANTIATE;
            assert notInstanciated.contains(offset);
            notInstanciated.swap(offset);
            if (reactOnRemoval) {
                delta.add(1 - value, cause);
            }
            mValue = value;
            model.getSolver().getExplainer().instantiateTo(this, value, cause, 0, 1);
            this.notifyPropagators(e, cause);
            return true;
        }
        return false;
    }

    /**
     * Updates the lower bound of the domain of {@code this} to {@code value}.
     * The instruction comes from {@code propagator}.
     * <ul>
     * <li>If {@code value} is smaller than the lower bound of the domain, nothing is done and the return value is {@code false},</li>
     * <li>if updating the lower bound to {@code value} leads to a dead-end (domain wipe-out),
     * a {@code ContradictionException} is thrown,</li>
     * <li>otherwise, if updating the lower bound to {@code value} can be done safely,
     * the event type is created (the original event can be promoted) and observers are notified
     * and the return value is {@code true}</li>
     * </ul>
     *
     * @param value new lower bound (included)
     * @param cause updating releaser
     * @return true if the lower bound has been updated, false otherwise
     * @throws ContradictionException if the domain become empty due to this action
     */
    @Override
    public boolean updateLowerBound(int value, ICause cause) throws ContradictionException {
        assert cause != null;
        return value > 0 && instantiateTo(value, cause);
    }

    /**
     * Updates the upper bound of the domain of {@code this} to {@code value}.
     * The instruction comes from {@code propagator}.
     * <ul>
     * <li>If {@code value} is greater than the upper bound of the domain, nothing is done and the return value is {@code false},</li>
     * <li>if updating the upper bound to {@code value} leads to a dead-end (domain wipe-out),
     * a {@code ContradictionException} is thrown,</li>
     * <li>otherwise, if updating the upper bound to {@code value} can be done safely,
     * the event type is created (the original event can be promoted) and observers are notified
     * and the return value is {@code true}</li>
     * </ul>
     *
     * @param value new upper bound (included)
     * @param cause update releaser
     * @return true if the upper bound has been updated, false otherwise
     * @throws ContradictionException if the domain become empty due to this action
     */
    @Override
    public boolean updateUpperBound(int value, ICause cause) throws ContradictionException {
        assert cause != null;
        return value < 1 && instantiateTo(value, cause);
    }

    @Override
    public boolean updateBounds(int lb, int ub, ICause cause) throws ContradictionException {
        boolean hasChanged = false;
        if (lb > 1 || ub < 0) {
            model.getSolver().getExplainer().instantiateTo(this, 2, cause, 0, 1);
            this.contradiction(cause, MSG_UNKNOWN);
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
    public boolean setToTrue(ICause cause) throws ContradictionException {
        assert cause != null;
        return instantiateTo(1, cause);
    }

    @Override
    public boolean setToFalse(ICause cause) throws ContradictionException {
        assert cause != null;
        return instantiateTo(0, cause);
    }

    @Override
    public boolean isInstantiated() {
        return !notInstanciated.contains(offset);
    }

    @Override
    public boolean isInstantiatedTo(int aValue) {
        return !notInstanciated.contains(offset) && mValue == aValue;
    }

    @Override
    public boolean contains(int aValue) {
        if (!notInstanciated.contains(offset)) {
            return mValue == aValue;
        }
        return aValue == 0 || aValue == 1;
    }

    /**
     * Retrieves the current value of the variable if instantiated, otherwier the lower bound.
     *
     * @return the current value (or lower bound if not yet instantiated).
     */
    @Override
    public int getValue() {
        assert isInstantiated() : name + " not instantiated";
        return getLB();
    }

    @Override
    public ESat getBooleanValue() {
        if (isInstantiated()) {
            return ESat.eval(getLB() != 0);
        }
        return ESat.UNDEFINED;
    }

    /**
     * Retrieves the lower bound of the variable
     *
     * @return the lower bound
     */
    @Override
    public int getLB() {
        if (!notInstanciated.contains(offset)) {
            return mValue;
        }
        return 0;
    }

    /**
     * Retrieves the upper bound of the variable
     *
     * @return the upper bound
     */
    @Override
    public int getUB() {
        if (!notInstanciated.contains(offset)) {
            return mValue;
        }
        return 1;
    }

    @Override
    public int getDomainSize() {
        return (notInstanciated.contains(offset) ? 2 : 1);
    }

    @Override
    public int getRange() {
        return getDomainSize();
    }

    @Override
    public int nextValue(int v) {
        if (!notInstanciated.contains(offset)) {
            final int val = mValue;
            return (val > v) ? val : Integer.MAX_VALUE;
        } else {
            if (v < 0) return 0;
            if (v == 0) return 1;
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public int nextValueOut(int v) {
        int lb = 0, ub = 1;
        if(!notInstanciated.contains(offset)){ // if this is instantiated
            lb = ub = mValue;
        }
        if (lb - 1 <= v && v <= ub) {
            return ub + 1;
        }else{
        return v + 1;
    }
    }

    @Override
    public int previousValue(int v) {
        if (v > getUB()) return getUB();
        if (v > getLB()) return getLB();
        return Integer.MIN_VALUE;
    }

    @Override
    public int previousValueOut(int v) {
        int lb = 0, ub = 1;
        if(!notInstanciated.contains(offset)){ // if this is instantiated
            lb = ub = mValue;
        }
        if (lb <= v && v <= ub + 1) {
            return lb - 1;
        }else{
        return v - 1;
    }
    }

    @Override
    public boolean hasEnumeratedDomain() {
        return true;
    }

    @Override
    public IEnumDelta getDelta() {
        return delta;
    }

    @Override
    public String toString() {
        if (!notInstanciated.contains(offset)) {
            return this.name + " = " + Integer.toString(mValue);
        } else {
            return this.name + " = " + "[0,1]";
        }
    }

    ////////////////////////////////////////////////////////////////
    ///// methode liees au fait qu'une variable est observable /////
    ////////////////////////////////////////////////////////////////

    @Override
    public void createDelta() {
        if (!reactOnRemoval) {
            delta = new OneValueDelta(model.getEnvironment());
            reactOnRemoval = true;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public IIntDeltaMonitor monitorDelta(ICause propagator) {
        createDelta();
        return new OneValueDeltaMonitor(delta, propagator);
    }

    @Override
    public void notifyMonitors(IEventType event) throws ContradictionException {
        for (int i = mIdx - 1; i >= 0; i--) {
            //noinspection unchecked
            monitors[i].onUpdate(this, event);
        }
    }

    @Override
    public int getTypeAndKind() {
        return VAR | BOOL;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public DisposableValueIterator getValueIterator(boolean bottomUp) {
        if (_viterator == null || _viterator.isNotReusable()) {
            _viterator = new DisposableValueBoundIterator(this);
        }
        if (bottomUp) {
            _viterator.bottomUpInit();
        } else {
            _viterator.topDownInit();
        }
        return _viterator;
    }

    @Override
    public DisposableRangeIterator getRangeIterator(boolean bottomUp) {
        if (_riterator == null || _riterator.isNotReusable()) {
            _riterator = new DisposableRangeBoundIterator(this);
        }
        if (bottomUp) {
            _riterator.bottomUpInit();
        } else {
            _riterator.topDownInit();
        }
        return _riterator;
    }

    @Override
    public Iterator<Integer> iterator() {
        if(_javaIterator == null){
            _javaIterator = new IntVarValueIterator(this);
        }
        _javaIterator.reset();
        return _javaIterator;
    }

    @Override
    public void _setNot(BoolVar neg) {
        this.not = neg;
    }

    @Override
    public BoolVar not() {
        if (!hasNot()) {
            not = model.boolNotView(this);
            not._setNot(this);
        }
        return not;
    }

    @Override
    public boolean hasNot() {
        return not != null;
    }

    @Override
    public boolean isLit() {
        return true;
    }

    @Override
    public boolean isNot() {
        return isNot;
    }

    @Override
    public void setNot(boolean isNot) {
        this.isNot = isNot;
    }
}
