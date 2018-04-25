/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.recoveryservices.implementation;

import com.microsoft.azure.management.recoveryservices.StorageType;
import com.microsoft.azure.management.recoveryservices.StorageTypeState;
import com.microsoft.azure.management.recoveryservices.EnhancedSecurityState;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.rest.serializer.JsonFlatten;
import com.microsoft.azure.Resource;

/**
 * Backup vault config details.
 */
@JsonFlatten
public class BackupVaultConfigInner extends Resource {
    /**
     * Storage type. Possible values include: 'Invalid', 'GeoRedundant',
     * 'LocallyRedundant'.
     */
    @JsonProperty(value = "properties.storageType")
    private StorageType storageType;

    /**
     * Locked or Unlocked. Once a machine is registered against a resource, the
     * storageTypeState is always Locked. Possible values include: 'Invalid',
     * 'Locked', 'Unlocked'.
     */
    @JsonProperty(value = "properties.storageTypeState")
    private StorageTypeState storageTypeState;

    /**
     * Enabled or Disabled. Possible values include: 'Invalid', 'Enabled',
     * 'Disabled'.
     */
    @JsonProperty(value = "properties.enhancedSecurityState")
    private EnhancedSecurityState enhancedSecurityState;

    /**
     * Get the storageType value.
     *
     * @return the storageType value
     */
    public StorageType storageType() {
        return this.storageType;
    }

    /**
     * Set the storageType value.
     *
     * @param storageType the storageType value to set
     * @return the BackupVaultConfigInner object itself.
     */
    public BackupVaultConfigInner withStorageType(StorageType storageType) {
        this.storageType = storageType;
        return this;
    }

    /**
     * Get the storageTypeState value.
     *
     * @return the storageTypeState value
     */
    public StorageTypeState storageTypeState() {
        return this.storageTypeState;
    }

    /**
     * Set the storageTypeState value.
     *
     * @param storageTypeState the storageTypeState value to set
     * @return the BackupVaultConfigInner object itself.
     */
    public BackupVaultConfigInner withStorageTypeState(StorageTypeState storageTypeState) {
        this.storageTypeState = storageTypeState;
        return this;
    }

    /**
     * Get the enhancedSecurityState value.
     *
     * @return the enhancedSecurityState value
     */
    public EnhancedSecurityState enhancedSecurityState() {
        return this.enhancedSecurityState;
    }

    /**
     * Set the enhancedSecurityState value.
     *
     * @param enhancedSecurityState the enhancedSecurityState value to set
     * @return the BackupVaultConfigInner object itself.
     */
    public BackupVaultConfigInner withEnhancedSecurityState(EnhancedSecurityState enhancedSecurityState) {
        this.enhancedSecurityState = enhancedSecurityState;
        return this;
    }

}
