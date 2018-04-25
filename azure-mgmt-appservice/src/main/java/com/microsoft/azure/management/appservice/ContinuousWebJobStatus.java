/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.appservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines values for ContinuousWebJobStatus.
 */
public enum ContinuousWebJobStatus {
    /** Enum value Initializing. */
    INITIALIZING("Initializing"),

    /** Enum value Starting. */
    STARTING("Starting"),

    /** Enum value Running. */
    RUNNING("Running"),

    /** Enum value PendingRestart. */
    PENDING_RESTART("PendingRestart"),

    /** Enum value Stopped. */
    STOPPED("Stopped");

    /** The actual serialized value for a ContinuousWebJobStatus instance. */
    private String value;

    ContinuousWebJobStatus(String value) {
        this.value = value;
    }

    /**
     * Parses a serialized value to a ContinuousWebJobStatus instance.
     *
     * @param value the serialized value to parse.
     * @return the parsed ContinuousWebJobStatus object, or null if unable to parse.
     */
    @JsonCreator
    public static ContinuousWebJobStatus fromString(String value) {
        ContinuousWebJobStatus[] items = ContinuousWebJobStatus.values();
        for (ContinuousWebJobStatus item : items) {
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
