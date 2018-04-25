/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.containerinstance;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The instance view of the container instance. Only valid in response.
 */
public class ContainerPropertiesInstanceView {
    /**
     * The number of times that the container instance has been restarted.
     */
    @JsonProperty(value = "restartCount", access = JsonProperty.Access.WRITE_ONLY)
    private Integer restartCount;

    /**
     * Current container instance state.
     */
    @JsonProperty(value = "currentState", access = JsonProperty.Access.WRITE_ONLY)
    private ContainerState currentState;

    /**
     * Previous container instance state.
     */
    @JsonProperty(value = "previousState", access = JsonProperty.Access.WRITE_ONLY)
    private ContainerState previousState;

    /**
     * The events of the container instance.
     */
    @JsonProperty(value = "events", access = JsonProperty.Access.WRITE_ONLY)
    private List<Event> events;

    /**
     * Get the restartCount value.
     *
     * @return the restartCount value
     */
    public Integer restartCount() {
        return this.restartCount;
    }

    /**
     * Get the currentState value.
     *
     * @return the currentState value
     */
    public ContainerState currentState() {
        return this.currentState;
    }

    /**
     * Get the previousState value.
     *
     * @return the previousState value
     */
    public ContainerState previousState() {
        return this.previousState;
    }

    /**
     * Get the events value.
     *
     * @return the events value
     */
    public List<Event> events() {
        return this.events;
    }

}
