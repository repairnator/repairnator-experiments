/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.apimanagement;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * API Management service resource SKU properties.
 */
public class ApiManagementServiceSkuProperties {
    /**
     * Name of the Sku. Possible values include: 'Developer', 'Standard',
     * 'Premium', 'Basic'.
     */
    @JsonProperty(value = "name", required = true)
    private SkuType name;

    /**
     * Capacity of the SKU (number of deployed units of the SKU). The default
     * value is 1.
     */
    @JsonProperty(value = "capacity")
    private Integer capacity;

    /**
     * Get the name value.
     *
     * @return the name value
     */
    public SkuType name() {
        return this.name;
    }

    /**
     * Set the name value.
     *
     * @param name the name value to set
     * @return the ApiManagementServiceSkuProperties object itself.
     */
    public ApiManagementServiceSkuProperties withName(SkuType name) {
        this.name = name;
        return this;
    }

    /**
     * Get the capacity value.
     *
     * @return the capacity value
     */
    public Integer capacity() {
        return this.capacity;
    }

    /**
     * Set the capacity value.
     *
     * @param capacity the capacity value to set
     * @return the ApiManagementServiceSkuProperties object itself.
     */
    public ApiManagementServiceSkuProperties withCapacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

}
