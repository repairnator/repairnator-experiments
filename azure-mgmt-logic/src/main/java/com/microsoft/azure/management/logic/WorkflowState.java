/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines values for WorkflowState.
 */
public enum WorkflowState {
    /** Enum value NotSpecified. */
    NOT_SPECIFIED("NotSpecified"),

    /** Enum value Completed. */
    COMPLETED("Completed"),

    /** Enum value Enabled. */
    ENABLED("Enabled"),

    /** Enum value Disabled. */
    DISABLED("Disabled"),

    /** Enum value Deleted. */
    DELETED("Deleted"),

    /** Enum value Suspended. */
    SUSPENDED("Suspended");

    /** The actual serialized value for a WorkflowState instance. */
    private String value;

    WorkflowState(String value) {
        this.value = value;
    }

    /**
     * Parses a serialized value to a WorkflowState instance.
     *
     * @param value the serialized value to parse.
     * @return the parsed WorkflowState object, or null if unable to parse.
     */
    @JsonCreator
    public static WorkflowState fromString(String value) {
        WorkflowState[] items = WorkflowState.values();
        for (WorkflowState item : items) {
            if (item.toString().equalsIgnoreCase(value)) {
                return item;
            }
        }
        return null;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
