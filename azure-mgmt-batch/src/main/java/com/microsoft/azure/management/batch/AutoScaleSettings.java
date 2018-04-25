/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.batch;

import org.joda.time.Period;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AutoScale settings for the pool.
 */
public class AutoScaleSettings {
    /**
     * A formula for the desired number of compute nodes in the pool.
     */
    @JsonProperty(value = "formula", required = true)
    private String formula;

    /**
     * The time interval at which to automatically adjust the pool size
     * according to the autoscale formula.
     * If omitted, the default value is 15 minutes (PT15M).
     */
    @JsonProperty(value = "evaluationInterval")
    private Period evaluationInterval;

    /**
     * Get the formula value.
     *
     * @return the formula value
     */
    public String formula() {
        return this.formula;
    }

    /**
     * Set the formula value.
     *
     * @param formula the formula value to set
     * @return the AutoScaleSettings object itself.
     */
    public AutoScaleSettings withFormula(String formula) {
        this.formula = formula;
        return this;
    }

    /**
     * Get the evaluationInterval value.
     *
     * @return the evaluationInterval value
     */
    public Period evaluationInterval() {
        return this.evaluationInterval;
    }

    /**
     * Set the evaluationInterval value.
     *
     * @param evaluationInterval the evaluationInterval value to set
     * @return the AutoScaleSettings object itself.
     */
    public AutoScaleSettings withEvaluationInterval(Period evaluationInterval) {
        this.evaluationInterval = evaluationInterval;
        return this;
    }

}
