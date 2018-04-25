/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.batch;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Deployment configuration properties.
 */
public class DeploymentConfiguration {
    /**
     * The cloud service configuration for the pool.
     * This property and virtualMachineConfiguration are mutually exclusive and
     * one of the properties must be specified. This property cannot be
     * specified if the Batch account was created with its poolAllocationMode
     * property set to 'UserSubscription'.
     */
    @JsonProperty(value = "cloudServiceConfiguration")
    private CloudServiceConfiguration cloudServiceConfiguration;

    /**
     * The virtual machine configuration for the pool.
     * This property and cloudServiceConfiguration are mutually exclusive and
     * one of the properties must be specified.
     */
    @JsonProperty(value = "virtualMachineConfiguration")
    private VirtualMachineConfiguration virtualMachineConfiguration;

    /**
     * Get the cloudServiceConfiguration value.
     *
     * @return the cloudServiceConfiguration value
     */
    public CloudServiceConfiguration cloudServiceConfiguration() {
        return this.cloudServiceConfiguration;
    }

    /**
     * Set the cloudServiceConfiguration value.
     *
     * @param cloudServiceConfiguration the cloudServiceConfiguration value to set
     * @return the DeploymentConfiguration object itself.
     */
    public DeploymentConfiguration withCloudServiceConfiguration(CloudServiceConfiguration cloudServiceConfiguration) {
        this.cloudServiceConfiguration = cloudServiceConfiguration;
        return this;
    }

    /**
     * Get the virtualMachineConfiguration value.
     *
     * @return the virtualMachineConfiguration value
     */
    public VirtualMachineConfiguration virtualMachineConfiguration() {
        return this.virtualMachineConfiguration;
    }

    /**
     * Set the virtualMachineConfiguration value.
     *
     * @param virtualMachineConfiguration the virtualMachineConfiguration value to set
     * @return the DeploymentConfiguration object itself.
     */
    public DeploymentConfiguration withVirtualMachineConfiguration(VirtualMachineConfiguration virtualMachineConfiguration) {
        this.virtualMachineConfiguration = virtualMachineConfiguration;
        return this;
    }

}
