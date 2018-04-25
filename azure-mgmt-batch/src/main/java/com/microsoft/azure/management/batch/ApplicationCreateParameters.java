/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.batch;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Parameters for adding an Application.
 */
public class ApplicationCreateParameters {
    /**
     * A value indicating whether packages within the application may be
     * overwritten using the same version string.
     */
    @JsonProperty(value = "allowUpdates")
    private Boolean allowUpdates;

    /**
     * The display name for the application.
     */
    @JsonProperty(value = "displayName")
    private String displayName;

    /**
     * Get the allowUpdates value.
     *
     * @return the allowUpdates value
     */
    public Boolean allowUpdates() {
        return this.allowUpdates;
    }

    /**
     * Set the allowUpdates value.
     *
     * @param allowUpdates the allowUpdates value to set
     * @return the ApplicationCreateParameters object itself.
     */
    public ApplicationCreateParameters withAllowUpdates(Boolean allowUpdates) {
        this.allowUpdates = allowUpdates;
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
     * @return the ApplicationCreateParameters object itself.
     */
    public ApplicationCreateParameters withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

}
