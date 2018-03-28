/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.containerinstance;

import org.joda.time.DateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The container instance state.
 */
public class ContainerState {
    /**
     * The state of the container instance.
     */
    @JsonProperty(value = "state")
    private String state;

    /**
     * The date-time when the container instance state started.
     */
    @JsonProperty(value = "startTime")
    private DateTime startTime;

    /**
     * The container instance exit codes correspond to those from the `docker
     * run` command.
     */
    @JsonProperty(value = "exitCode")
    private Integer exitCode;

    /**
     * The date-time when the container instance state finished.
     */
    @JsonProperty(value = "finishTime")
    private DateTime finishTime;

    /**
     * The human-readable status of the container instance state.
     */
    @JsonProperty(value = "detailStatus")
    private String detailStatus;

    /**
     * Get the state value.
     *
     * @return the state value
     */
    public String state() {
        return this.state;
    }

    /**
     * Set the state value.
     *
     * @param state the state value to set
     * @return the ContainerState object itself.
     */
    public ContainerState withState(String state) {
        this.state = state;
        return this;
    }

    /**
     * Get the startTime value.
     *
     * @return the startTime value
     */
    public DateTime startTime() {
        return this.startTime;
    }

    /**
     * Set the startTime value.
     *
     * @param startTime the startTime value to set
     * @return the ContainerState object itself.
     */
    public ContainerState withStartTime(DateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    /**
     * Get the exitCode value.
     *
     * @return the exitCode value
     */
    public Integer exitCode() {
        return this.exitCode;
    }

    /**
     * Set the exitCode value.
     *
     * @param exitCode the exitCode value to set
     * @return the ContainerState object itself.
     */
    public ContainerState withExitCode(Integer exitCode) {
        this.exitCode = exitCode;
        return this;
    }

    /**
     * Get the finishTime value.
     *
     * @return the finishTime value
     */
    public DateTime finishTime() {
        return this.finishTime;
    }

    /**
     * Set the finishTime value.
     *
     * @param finishTime the finishTime value to set
     * @return the ContainerState object itself.
     */
    public ContainerState withFinishTime(DateTime finishTime) {
        this.finishTime = finishTime;
        return this;
    }

    /**
     * Get the detailStatus value.
     *
     * @return the detailStatus value
     */
    public String detailStatus() {
        return this.detailStatus;
    }

    /**
     * Set the detailStatus value.
     *
     * @param detailStatus the detailStatus value to set
     * @return the ContainerState object itself.
     */
    public ContainerState withDetailStatus(String detailStatus) {
        this.detailStatus = detailStatus;
        return this;
    }

}
