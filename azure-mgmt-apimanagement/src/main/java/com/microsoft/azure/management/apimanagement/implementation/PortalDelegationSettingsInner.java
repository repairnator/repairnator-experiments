/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.apimanagement.implementation;

import com.microsoft.azure.management.apimanagement.SubscriptionsDelegationSettingsProperties;
import com.microsoft.azure.management.apimanagement.RegistrationDelegationSettingsProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.rest.serializer.JsonFlatten;
import com.microsoft.azure.Resource;

/**
 * Delegation settings for a developer portal.
 */
@JsonFlatten
public class PortalDelegationSettingsInner extends Resource {
    /**
     * A delegation Url.
     */
    @JsonProperty(value = "properties.url")
    private String url;

    /**
     * A base64-encoded validation key to validate, that a request is coming
     * from Azure API Management.
     */
    @JsonProperty(value = "properties.validationKey")
    private String validationKey;

    /**
     * Subscriptions delegation settings.
     */
    @JsonProperty(value = "properties.subscriptions")
    private SubscriptionsDelegationSettingsProperties subscriptions;

    /**
     * User registration delegation settings.
     */
    @JsonProperty(value = "properties.userRegistration")
    private RegistrationDelegationSettingsProperties userRegistration;

    /**
     * Get the url value.
     *
     * @return the url value
     */
    public String url() {
        return this.url;
    }

    /**
     * Set the url value.
     *
     * @param url the url value to set
     * @return the PortalDelegationSettingsInner object itself.
     */
    public PortalDelegationSettingsInner withUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * Get the validationKey value.
     *
     * @return the validationKey value
     */
    public String validationKey() {
        return this.validationKey;
    }

    /**
     * Set the validationKey value.
     *
     * @param validationKey the validationKey value to set
     * @return the PortalDelegationSettingsInner object itself.
     */
    public PortalDelegationSettingsInner withValidationKey(String validationKey) {
        this.validationKey = validationKey;
        return this;
    }

    /**
     * Get the subscriptions value.
     *
     * @return the subscriptions value
     */
    public SubscriptionsDelegationSettingsProperties subscriptions() {
        return this.subscriptions;
    }

    /**
     * Set the subscriptions value.
     *
     * @param subscriptions the subscriptions value to set
     * @return the PortalDelegationSettingsInner object itself.
     */
    public PortalDelegationSettingsInner withSubscriptions(SubscriptionsDelegationSettingsProperties subscriptions) {
        this.subscriptions = subscriptions;
        return this;
    }

    /**
     * Get the userRegistration value.
     *
     * @return the userRegistration value
     */
    public RegistrationDelegationSettingsProperties userRegistration() {
        return this.userRegistration;
    }

    /**
     * Set the userRegistration value.
     *
     * @param userRegistration the userRegistration value to set
     * @return the PortalDelegationSettingsInner object itself.
     */
    public PortalDelegationSettingsInner withUserRegistration(RegistrationDelegationSettingsProperties userRegistration) {
        this.userRegistration = userRegistration;
        return this;
    }

}
