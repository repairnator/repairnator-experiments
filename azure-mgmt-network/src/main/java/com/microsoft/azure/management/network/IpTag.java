/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.network;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains the IpTag associated with the public IP address.
 */
public class IpTag {
    /**
     * Gets or sets the ipTag type: Example FirstPartyUsage.
     */
    @JsonProperty(value = "ipTagType")
    private String ipTagType;

    /**
     * Gets or sets value of the IpTag associated with the public IP. Example
     * SQL, Storage etc.
     */
    @JsonProperty(value = "tag")
    private String tag;

    /**
     * Get the ipTagType value.
     *
     * @return the ipTagType value
     */
    public String ipTagType() {
        return this.ipTagType;
    }

    /**
     * Set the ipTagType value.
     *
     * @param ipTagType the ipTagType value to set
     * @return the IpTag object itself.
     */
    public IpTag withIpTagType(String ipTagType) {
        this.ipTagType = ipTagType;
        return this;
    }

    /**
     * Get the tag value.
     *
     * @return the tag value
     */
    public String tag() {
        return this.tag;
    }

    /**
     * Set the tag value.
     *
     * @param tag the tag value to set
     * @return the IpTag object itself.
     */
    public IpTag withTag(String tag) {
        this.tag = tag;
        return this;
    }

}
