/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.recoveryservices;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines values for StorageModelType.
 */
public final class StorageModelType {
    /** Static value Invalid for StorageModelType. */
    public static final StorageModelType INVALID = new StorageModelType("Invalid");

    /** Static value GeoRedundant for StorageModelType. */
    public static final StorageModelType GEO_REDUNDANT = new StorageModelType("GeoRedundant");

    /** Static value LocallyRedundant for StorageModelType. */
    public static final StorageModelType LOCALLY_REDUNDANT = new StorageModelType("LocallyRedundant");

    private String value;

    /**
     * Creates a custom value for StorageModelType.
     * @param value the custom value
     */
    public StorageModelType(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StorageModelType)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        StorageModelType rhs = (StorageModelType) obj;
        if (value == null) {
            return rhs.value == null;
        } else {
            return value.equals(rhs.value);
        }
    }
}
