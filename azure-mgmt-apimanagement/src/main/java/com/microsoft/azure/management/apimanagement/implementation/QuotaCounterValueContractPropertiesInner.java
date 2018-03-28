/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.apimanagement.implementation;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Quota counter value details.
 */
public class QuotaCounterValueContractPropertiesInner {
    /**
     * Number of times Counter was called.
     */
    @JsonProperty(value = "callsCount")
    private Integer callsCount;

    /**
     * Data Transferred in KiloBytes.
     */
    @JsonProperty(value = "kbTransferred")
    private Double kbTransferred;

    /**
     * Get the callsCount value.
     *
     * @return the callsCount value
     */
    public Integer callsCount() {
        return this.callsCount;
    }

    /**
     * Set the callsCount value.
     *
     * @param callsCount the callsCount value to set
     * @return the QuotaCounterValueContractPropertiesInner object itself.
     */
    public QuotaCounterValueContractPropertiesInner withCallsCount(Integer callsCount) {
        this.callsCount = callsCount;
        return this;
    }

    /**
     * Get the kbTransferred value.
     *
     * @return the kbTransferred value
     */
    public Double kbTransferred() {
        return this.kbTransferred;
    }

    /**
     * Set the kbTransferred value.
     *
     * @param kbTransferred the kbTransferred value to set
     * @return the QuotaCounterValueContractPropertiesInner object itself.
     */
    public QuotaCounterValueContractPropertiesInner withKbTransferred(Double kbTransferred) {
        this.kbTransferred = kbTransferred;
        return this;
    }

}
