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
 * Defines values for TransparentDataEncryptionStatus.
 */
public enum TransparentDataEncryptionStates {
    /** Enum value Enabled. */
    ENABLED("Enabled"),

    /** Enum value Disabled. */
    DISABLED("Disabled");

    /** The actual serialized value for a TransparentDataEncryptionStatus instance. */
    private String value;

    TransparentDataEncryptionStates(String value) {
        this.value = value;
    }

    /**
     * Parses a serialized value to a TransparentDataEncryptionStatus instance.
     *
     * @param value the serialized value to parse.
     * @return the parsed TransparentDataEncryptionStatus object, or null if unable to parse.
     */
    @JsonCreator
    public static TransparentDataEncryptionStates fromString(String value) {
        TransparentDataEncryptionStates[] items = TransparentDataEncryptionStates.values();
        for (TransparentDataEncryptionStates item : items) {
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
