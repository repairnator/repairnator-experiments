/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.devtestlab;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines values for CustomImageOsType.
 */
public final class CustomImageOsType {
    /** Static value Windows for CustomImageOsType. */
    public static final CustomImageOsType WINDOWS = new CustomImageOsType("Windows");

    /** Static value Linux for CustomImageOsType. */
    public static final CustomImageOsType LINUX = new CustomImageOsType("Linux");

    /** Static value None for CustomImageOsType. */
    public static final CustomImageOsType NONE = new CustomImageOsType("None");

    private String value;

    /**
     * Creates a custom value for CustomImageOsType.
     * @param value the custom value
     */
    public CustomImageOsType(String value) {
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
        if (!(obj instanceof CustomImageOsType)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        CustomImageOsType rhs = (CustomImageOsType) obj;
        if (value == null) {
            return rhs.value == null;
        } else {
            return value.equals(rhs.value);
        }
    }
}
