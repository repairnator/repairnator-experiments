/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.apimanagement.implementation;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.rest.serializer.JsonFlatten;

/**
 * Property update Parameters.
 */
@JsonFlatten
public class PropertyUpdateParametersInner {
    /**
     * Optional tags that when provided can be used to filter the property
     * list.
     */
    @JsonProperty(value = "properties.tags")
    private List<String> tags;

    /**
     * Determines whether the value is a secret and should be encrypted or not.
     * Default value is false.
     */
    @JsonProperty(value = "properties.secret")
    private Boolean secret;

    /**
     * Unique name of Property. It may contain only letters, digits, period,
     * dash, and underscore characters.
     */
    @JsonProperty(value = "properties.displayName")
    private String displayName;

    /**
     * Value of the property. Can contain policy expressions. It may not be
     * empty or consist only of whitespace.
     */
    @JsonProperty(value = "properties.value")
    private String value;

    /**
     * Get the tags value.
     *
     * @return the tags value
     */
    public List<String> tags() {
        return this.tags;
    }

    /**
     * Set the tags value.
     *
     * @param tags the tags value to set
     * @return the PropertyUpdateParametersInner object itself.
     */
    public PropertyUpdateParametersInner withTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    /**
     * Get the secret value.
     *
     * @return the secret value
     */
    public Boolean secret() {
        return this.secret;
    }

    /**
     * Set the secret value.
     *
     * @param secret the secret value to set
     * @return the PropertyUpdateParametersInner object itself.
     */
    public PropertyUpdateParametersInner withSecret(Boolean secret) {
        this.secret = secret;
        return this;
    }

    /**
     * Get the displayName value.
     *
     * @return the displayName value
     */
    public String displayName() {
        return this.displayName;
    }

    /**
     * Set the displayName value.
     *
     * @param displayName the displayName value to set
     * @return the PropertyUpdateParametersInner object itself.
     */
    public PropertyUpdateParametersInner withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Get the value value.
     *
     * @return the value value
     */
    public String value() {
        return this.value;
    }

    /**
     * Set the value value.
     *
     * @param value the value value to set
     * @return the PropertyUpdateParametersInner object itself.
     */
    public PropertyUpdateParametersInner withValue(String value) {
        this.value = value;
        return this;
    }

}
