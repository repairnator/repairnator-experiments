/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.monitor.implementation;

import com.microsoft.azure.management.monitor.CategoryType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.rest.serializer.JsonFlatten;
import com.microsoft.azure.management.monitor.ProxyOnlyResource;

/**
 * The diagnostic settings category resource.
 */
@JsonFlatten
public class DiagnosticSettingsCategoryResourceInner extends ProxyOnlyResource {
    /**
     * The type of the diagnostic settings category. Possible values include:
     * 'Metrics', 'Logs'.
     */
    @JsonProperty(value = "properties.categoryType")
    private CategoryType categoryType;

    /**
     * Get the categoryType value.
     *
     * @return the categoryType value
     */
    public CategoryType categoryType() {
        return this.categoryType;
    }

    /**
     * Set the categoryType value.
     *
     * @param categoryType the categoryType value to set
     * @return the DiagnosticSettingsCategoryResourceInner object itself.
     */
    public DiagnosticSettingsCategoryResourceInner withCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
        return this;
    }

}
