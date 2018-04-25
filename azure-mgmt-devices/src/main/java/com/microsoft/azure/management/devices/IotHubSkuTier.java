/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 * Changes may cause incorrect behavior and will be lost if the code is
 * regenerated.
 */

package com.microsoft.azure.management.devices;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines values for IotHubSkuTier.
 */
public enum IotHubSkuTier {
    /** Enum value Free. */
    FREE("Free"),

    /** Enum value Standard. */
    STANDARD("Standard");

    /** The actual serialized value for a IotHubSkuTier instance. */
    private String value;

    IotHubSkuTier(String value) {
        this.value = value;
    }

    /**
     * Parses a serialized value to a IotHubSkuTier instance.
     *
     * @param value the serialized value to parse.
     * @return the parsed IotHubSkuTier object, or null if unable to parse.
     */
    @JsonCreator
    public static IotHubSkuTier fromString(String value) {
        IotHubSkuTier[] items = IotHubSkuTier.values();
        for (IotHubSkuTier item : items) {
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
