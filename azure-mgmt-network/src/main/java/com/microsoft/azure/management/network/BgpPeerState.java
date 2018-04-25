/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.network;

import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.microsoft.rest.ExpandableStringEnum;

/**
 * Defines values for BgpPeerState.
 */
public final class BgpPeerState extends ExpandableStringEnum<BgpPeerState> {
    /** Static value Unknown for BgpPeerState. */
    public static final BgpPeerState UNKNOWN = fromString("Unknown");

    /** Static value Stopped for BgpPeerState. */
    public static final BgpPeerState STOPPED = fromString("Stopped");

    /** Static value Idle for BgpPeerState. */
    public static final BgpPeerState IDLE = fromString("Idle");

    /** Static value Connecting for BgpPeerState. */
    public static final BgpPeerState CONNECTING = fromString("Connecting");

    /** Static value Connected for BgpPeerState. */
    public static final BgpPeerState CONNECTED = fromString("Connected");

    /**
     * Creates or finds a BgpPeerState from its string representation.
     * @param name a name to look for
     * @return the corresponding BgpPeerState
     */
    @JsonCreator
    public static BgpPeerState fromString(String name) {
        return fromString(name, BgpPeerState.class);
    }

    /**
     * @return known BgpPeerState values
     */
    public static Collection<BgpPeerState> values() {
        return values(BgpPeerState.class);
    }
}
