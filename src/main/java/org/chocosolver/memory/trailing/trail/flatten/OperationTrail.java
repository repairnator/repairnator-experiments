/**
 * This file is part of choco-solver, http://choco-solver.org/
 *
 * Copyright (c) 2017, IMT Atlantique. All rights reserved.
 *
 * Licensed under the BSD 4-clause license.
 * See LICENSE file in the project root for full license information.
 */
package org.chocosolver.memory.trailing.trail.flatten;

import org.chocosolver.memory.structure.IOperation;
import org.chocosolver.memory.trailing.trail.IOperationTrail;

/**
 * Created by IntelliJ IDEA.
 * User: chameau
 * Date: 9 feb. 2011
 */
public class OperationTrail implements IOperationTrail {

    /**
     * Load factor
     */
    private final double loadfactor;

    /**
     * Stack of values (former values that need be restored upon backtracking).
     */
    private IOperation[] valueStack;


    /**
     * Points the level of the last entry.
     */
    private int currentLevel;


    /**
     * A stack of pointers (for each start of a world).
     */
    private int[] worldStartLevels;

    /**
     * Constructs a trail with predefined size.
     *
     * @param nUpdates maximal number of updates that will be stored
     * @param nWorlds  maximal number of worlds that will be stored
     * @param loadfactor load factor for structures
     */
    public OperationTrail(int nUpdates, int nWorlds, double loadfactor) {
        currentLevel = 0;
        valueStack = new IOperation[nUpdates];
        worldStartLevels = new int[nWorlds];
        this.loadfactor = loadfactor;
    }


    /**
     * Moving up to the next world.
     *
     * @param worldIndex current world index
     */
    public void worldPush(int worldIndex) {
        worldStartLevels[worldIndex] = currentLevel;
        if (worldIndex == worldStartLevels.length - 1) {
            resizeWorldCapacity((int) (worldStartLevels.length * loadfactor));
        }
    }


    /**
     * Moving down to the previous world.
     *
     * @param worldIndex current world index
     */
    public void worldPop(int worldIndex) {
        final int wsl = worldStartLevels[worldIndex];
        while (currentLevel > wsl) {
            valueStack[--currentLevel].undo();
        }
    }

    /**
     * Comits a world: merging it with the previous one.
     */
    public void worldCommit(int worldIndex) {
    }

    /**
     * Reacts when a StoredInt is modified: push the former value & timestamp
     * on the stacks.
     */
    public void savePreviousState(IOperation oldValue) {
        valueStack[currentLevel++] = oldValue;
        if (currentLevel == valueStack.length) {
            resizeUpdateCapacity();
        }
    }

    private void resizeUpdateCapacity() {
        final int newCapacity = (int) (valueStack.length * loadfactor);
        // First, copy the stack of former values
        final IOperation[] tmp2 = new IOperation[newCapacity];
        System.arraycopy(valueStack, 0, tmp2, 0, valueStack.length);
        valueStack = tmp2;
    }

    private void resizeWorldCapacity(int newWorldCapacity) {
        final int[] tmp = new int[newWorldCapacity];
        System.arraycopy(worldStartLevels, 0, tmp, 0, worldStartLevels.length);
        worldStartLevels = tmp;
    }
}
