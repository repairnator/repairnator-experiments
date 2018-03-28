/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.sql;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines values for AutomaticTuningServerMode.
 */
public enum AutomaticTuningServerMode {
    /** Enum value Custom. */
    CUSTOM("Custom"),

    /** Enum value Auto. */
    AUTO("Auto"),

    /** Enum value Unspecified. */
    UNSPECIFIED("Unspecified");

    /** The actual serialized value for a AutomaticTuningServerMode instance. */
    private String value;

    AutomaticTuningServerMode(String value) {
        this.value = value;
    }

    /**
     * Parses a serialized value to a AutomaticTuningServerMode instance.
     *
     * @param value the serialized value to parse.
     * @return the parsed AutomaticTuningServerMode object, or null if unable to parse.
     */
    @JsonCreator
    public static AutomaticTuningServerMode fromString(String value) {
        AutomaticTuningServerMode[] items = AutomaticTuningServerMode.values();
        for (AutomaticTuningServerMode item : items) {
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
