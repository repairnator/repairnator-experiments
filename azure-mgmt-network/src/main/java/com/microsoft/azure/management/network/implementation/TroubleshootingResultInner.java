/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.network.implementation;

import org.joda.time.DateTime;
import java.util.List;
import com.microsoft.azure.management.network.TroubleshootingDetails;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Troubleshooting information gained from specified resource.
 */
public class TroubleshootingResultInner {
    /**
     * The start time of the troubleshooting.
     */
    @JsonProperty(value = "startTime")
    private DateTime startTime;

    /**
     * The end time of the troubleshooting.
     */
    @JsonProperty(value = "endTime")
    private DateTime endTime;

    /**
     * The result code of the troubleshooting.
     */
    @JsonProperty(value = "code")
    private String code;

    /**
     * Information from troubleshooting.
     */
    @JsonProperty(value = "results")
    private List<TroubleshootingDetails> results;

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
     * @return the TroubleshootingResultInner object itself.
     */
    public TroubleshootingResultInner withStartTime(DateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    /**
     * Get the endTime value.
     *
     * @return the endTime value
     */
    public DateTime endTime() {
        return this.endTime;
    }

    /**
     * Set the endTime value.
     *
     * @param endTime the endTime value to set
     * @return the TroubleshootingResultInner object itself.
     */
    public TroubleshootingResultInner withEndTime(DateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    /**
     * Get the code value.
     *
     * @return the code value
     */
    public String code() {
        return this.code;
    }

    /**
     * Set the code value.
     *
     * @param code the code value to set
     * @return the TroubleshootingResultInner object itself.
     */
    public TroubleshootingResultInner withCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * Get the results value.
     *
     * @return the results value
     */
    public List<TroubleshootingDetails> results() {
        return this.results;
    }

    /**
     * Set the results value.
     *
     * @param results the results value to set
     * @return the TroubleshootingResultInner object itself.
     */
    public TroubleshootingResultInner withResults(List<TroubleshootingDetails> results) {
        this.results = results;
        return this;
    }

}
