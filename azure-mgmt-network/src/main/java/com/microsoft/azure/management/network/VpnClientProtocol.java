/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.network;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines values for VpnClientProtocol.
 */
public final class VpnClientProtocol {
    /** Static value IkeV2 for VpnClientProtocol. */
    public static final VpnClientProtocol IKE_V2 = new VpnClientProtocol("IkeV2");

    /** Static value SSTP for VpnClientProtocol. */
    public static final VpnClientProtocol SSTP = new VpnClientProtocol("SSTP");

    private String value;

    /**
     * Creates a custom value for VpnClientProtocol.
     * @param value the custom value
     */
    public VpnClientProtocol(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof VpnClientProtocol)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        VpnClientProtocol rhs = (VpnClientProtocol) obj;
        if (value == null) {
            return rhs.value == null;
        } else {
            return value.equals(rhs.value);
        }
    }
}
