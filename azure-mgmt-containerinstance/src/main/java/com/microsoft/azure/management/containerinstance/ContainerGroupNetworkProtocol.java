/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.containerinstance;

import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.microsoft.rest.ExpandableStringEnum;

/**
 * Defines values for ContainerGroupNetworkProtocol.
 */
public final class ContainerGroupNetworkProtocol extends ExpandableStringEnum<ContainerGroupNetworkProtocol> {
    /** Static value TCP for ContainerGroupNetworkProtocol. */
    public static final ContainerGroupNetworkProtocol TCP = fromString("TCP");

    /** Static value UDP for ContainerGroupNetworkProtocol. */
    public static final ContainerGroupNetworkProtocol UDP = fromString("UDP");

    /**
     * Creates or finds a ContainerGroupNetworkProtocol from its string representation.
     * @param name a name to look for
     * @return the corresponding ContainerGroupNetworkProtocol
     */
    @JsonCreator
    public static ContainerGroupNetworkProtocol fromString(String name) {
        return fromString(name, ContainerGroupNetworkProtocol.class);
    }

    /**
     * @return known ContainerGroupNetworkProtocol values
     */
    public static Collection<ContainerGroupNetworkProtocol> values() {
        return values(ContainerGroupNetworkProtocol.class);
    }
}
