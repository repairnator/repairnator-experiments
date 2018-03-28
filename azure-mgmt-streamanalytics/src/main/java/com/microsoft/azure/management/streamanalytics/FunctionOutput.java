/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.streamanalytics;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Describes the output of a function.
 */
public class FunctionOutput {
    /**
     * The (Azure Stream Analytics supported) data type of the function output.
     * A list of valid Azure Stream Analytics data types are described at
     * https://msdn.microsoft.com/en-us/library/azure/dn835065.aspx.
     */
    @JsonProperty(value = "dataType")
    private String dataType;

    /**
     * Get the dataType value.
     *
     * @return the dataType value
     */
    public String dataType() {
        return this.dataType;
    }

    /**
     * Set the dataType value.
     *
     * @param dataType the dataType value to set
     * @return the FunctionOutput object itself.
     */
    public FunctionOutput withDataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

}
