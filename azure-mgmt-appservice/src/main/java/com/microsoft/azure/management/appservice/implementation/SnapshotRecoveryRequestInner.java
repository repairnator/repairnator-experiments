/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.appservice.implementation;

import com.microsoft.azure.management.appservice.SnapshotRecoveryTarget;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.rest.serializer.JsonFlatten;
import com.microsoft.azure.management.appservice.ProxyOnlyResource;

/**
 * Details about app recovery operation.
 */
@JsonFlatten
public class SnapshotRecoveryRequestInner extends ProxyOnlyResource {
    /**
     * Point in time in which the app recovery should be attempted, formatted
     * as a DateTime string.
     */
    @JsonProperty(value = "properties.snapshotTime")
    private String snapshotTime;

    /**
     * Specifies the web app that snapshot contents will be written to.
     */
    @JsonProperty(value = "properties.recoveryTarget")
    private SnapshotRecoveryTarget recoveryTarget;

    /**
     * If &lt;code&gt;true&lt;/code&gt; the recovery operation can overwrite
     * source app; otherwise, &lt;code&gt;false&lt;/code&gt;.
     */
    @JsonProperty(value = "properties.overwrite", required = true)
    private boolean overwrite;

    /**
     * If true, site configuration, in addition to content, will be reverted.
     */
    @JsonProperty(value = "properties.recoverConfiguration")
    private Boolean recoverConfiguration;

    /**
     * If true, custom hostname conflicts will be ignored when recovering to a
     * target web app.
     * This setting is only necessary when RecoverConfiguration is enabled.
     */
    @JsonProperty(value = "properties.ignoreConflictingHostNames")
    private Boolean ignoreConflictingHostNames;

    /**
     * Get the snapshotTime value.
     *
     * @return the snapshotTime value
     */
    public String snapshotTime() {
        return this.snapshotTime;
    }

    /**
     * Set the snapshotTime value.
     *
     * @param snapshotTime the snapshotTime value to set
     * @return the SnapshotRecoveryRequestInner object itself.
     */
    public SnapshotRecoveryRequestInner withSnapshotTime(String snapshotTime) {
        this.snapshotTime = snapshotTime;
        return this;
    }

    /**
     * Get the recoveryTarget value.
     *
     * @return the recoveryTarget value
     */
    public SnapshotRecoveryTarget recoveryTarget() {
        return this.recoveryTarget;
    }

    /**
     * Set the recoveryTarget value.
     *
     * @param recoveryTarget the recoveryTarget value to set
     * @return the SnapshotRecoveryRequestInner object itself.
     */
    public SnapshotRecoveryRequestInner withRecoveryTarget(SnapshotRecoveryTarget recoveryTarget) {
        this.recoveryTarget = recoveryTarget;
        return this;
    }

    /**
     * Get the overwrite value.
     *
     * @return the overwrite value
     */
    public boolean overwrite() {
        return this.overwrite;
    }

    /**
     * Set the overwrite value.
     *
     * @param overwrite the overwrite value to set
     * @return the SnapshotRecoveryRequestInner object itself.
     */
    public SnapshotRecoveryRequestInner withOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
        return this;
    }

    /**
     * Get the recoverConfiguration value.
     *
     * @return the recoverConfiguration value
     */
    public Boolean recoverConfiguration() {
        return this.recoverConfiguration;
    }

    /**
     * Set the recoverConfiguration value.
     *
     * @param recoverConfiguration the recoverConfiguration value to set
     * @return the SnapshotRecoveryRequestInner object itself.
     */
    public SnapshotRecoveryRequestInner withRecoverConfiguration(Boolean recoverConfiguration) {
        this.recoverConfiguration = recoverConfiguration;
        return this;
    }

    /**
     * Get the ignoreConflictingHostNames value.
     *
     * @return the ignoreConflictingHostNames value
     */
    public Boolean ignoreConflictingHostNames() {
        return this.ignoreConflictingHostNames;
    }

    /**
     * Set the ignoreConflictingHostNames value.
     *
     * @param ignoreConflictingHostNames the ignoreConflictingHostNames value to set
     * @return the SnapshotRecoveryRequestInner object itself.
     */
    public SnapshotRecoveryRequestInner withIgnoreConflictingHostNames(Boolean ignoreConflictingHostNames) {
        this.ignoreConflictingHostNames = ignoreConflictingHostNames;
        return this;
    }

}
