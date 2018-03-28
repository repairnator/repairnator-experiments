/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.monitor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines values for MetricStatisticType.
 */
public enum MetricStatisticType {
    /** Enum value Average. */
    AVERAGE("Average"),

    /** Enum value Min. */
    MIN("Min"),

    /** Enum value Max. */
    MAX("Max"),

    /** Enum value Sum. */
    SUM("Sum");

    /** The actual serialized value for a MetricStatisticType instance. */
    private String value;

    MetricStatisticType(String value) {
        this.value = value;
    }

    /**
     * Parses a serialized value to a MetricStatisticType instance.
     *
     * @param value the serialized value to parse.
     * @return the parsed MetricStatisticType object, or null if unable to parse.
     */
    @JsonCreator
    public static MetricStatisticType fromString(String value) {
        MetricStatisticType[] items = MetricStatisticType.values();
        for (MetricStatisticType item : items) {
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
