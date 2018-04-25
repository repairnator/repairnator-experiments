/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.apimanagement.implementation;

import org.joda.time.DateTime;
import com.microsoft.azure.management.apimanagement.SubscriptionState;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.rest.serializer.JsonFlatten;

/**
 * Subscription update details.
 */
@JsonFlatten
public class SubscriptionUpdateParametersInner {
    /**
     * User identifier path: /users/{uid}.
     */
    @JsonProperty(value = "properties.userId")
    private String userId;

    /**
     * Product identifier path: /products/{productId}.
     */
    @JsonProperty(value = "properties.productId")
    private String productId;

    /**
     * Subscription expiration date. The setting is for audit purposes only and
     * the subscription is not automatically expired. The subscription
     * lifecycle can be managed by using the `state` property. The date
     * conforms to the following format: `yyyy-MM-ddTHH:mm:ssZ` as specified by
     * the ISO 8601 standard.
     */
    @JsonProperty(value = "properties.expirationDate")
    private DateTime expirationDate;

    /**
     * Subscription name.
     */
    @JsonProperty(value = "properties.displayName")
    private String displayName;

    /**
     * Primary subscription key.
     */
    @JsonProperty(value = "properties.primaryKey")
    private String primaryKey;

    /**
     * Secondary subscription key.
     */
    @JsonProperty(value = "properties.secondaryKey")
    private String secondaryKey;

    /**
     * Subscription state. Possible states are * active – the subscription is
     * active, * suspended – the subscription is blocked, and the subscriber
     * cannot call any APIs of the product, * submitted – the subscription
     * request has been made by the developer, but has not yet been approved or
     * rejected, * rejected – the subscription request has been denied by an
     * administrator, * cancelled – the subscription has been cancelled by the
     * developer or administrator, * expired – the subscription reached its
     * expiration date and was deactivated. Possible values include:
     * 'suspended', 'active', 'expired', 'submitted', 'rejected', 'cancelled'.
     */
    @JsonProperty(value = "properties.state")
    private SubscriptionState state;

    /**
     * Comments describing subscription state change by the administrator.
     */
    @JsonProperty(value = "properties.stateComment")
    private String stateComment;

    /**
     * Get the userId value.
     *
     * @return the userId value
     */
    public String userId() {
        return this.userId;
    }

    /**
     * Set the userId value.
     *
     * @param userId the userId value to set
     * @return the SubscriptionUpdateParametersInner object itself.
     */
    public SubscriptionUpdateParametersInner withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    /**
     * Get the productId value.
     *
     * @return the productId value
     */
    public String productId() {
        return this.productId;
    }

    /**
     * Set the productId value.
     *
     * @param productId the productId value to set
     * @return the SubscriptionUpdateParametersInner object itself.
     */
    public SubscriptionUpdateParametersInner withProductId(String productId) {
        this.productId = productId;
        return this;
    }

    /**
     * Get the expirationDate value.
     *
     * @return the expirationDate value
     */
    public DateTime expirationDate() {
        return this.expirationDate;
    }

    /**
     * Set the expirationDate value.
     *
     * @param expirationDate the expirationDate value to set
     * @return the SubscriptionUpdateParametersInner object itself.
     */
    public SubscriptionUpdateParametersInner withExpirationDate(DateTime expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    /**
     * Get the displayName value.
     *
     * @return the displayName value
     */
    public String displayName() {
        return this.displayName;
    }

    /**
     * Set the displayName value.
     *
     * @param displayName the displayName value to set
     * @return the SubscriptionUpdateParametersInner object itself.
     */
    public SubscriptionUpdateParametersInner withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Get the primaryKey value.
     *
     * @return the primaryKey value
     */
    public String primaryKey() {
        return this.primaryKey;
    }

    /**
     * Set the primaryKey value.
     *
     * @param primaryKey the primaryKey value to set
     * @return the SubscriptionUpdateParametersInner object itself.
     */
    public SubscriptionUpdateParametersInner withPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
        return this;
    }

    /**
     * Get the secondaryKey value.
     *
     * @return the secondaryKey value
     */
    public String secondaryKey() {
        return this.secondaryKey;
    }

    /**
     * Set the secondaryKey value.
     *
     * @param secondaryKey the secondaryKey value to set
     * @return the SubscriptionUpdateParametersInner object itself.
     */
    public SubscriptionUpdateParametersInner withSecondaryKey(String secondaryKey) {
        this.secondaryKey = secondaryKey;
        return this;
    }

    /**
     * Get the state value.
     *
     * @return the state value
     */
    public SubscriptionState state() {
        return this.state;
    }

    /**
     * Set the state value.
     *
     * @param state the state value to set
     * @return the SubscriptionUpdateParametersInner object itself.
     */
    public SubscriptionUpdateParametersInner withState(SubscriptionState state) {
        this.state = state;
        return this;
    }

    /**
     * Get the stateComment value.
     *
     * @return the stateComment value
     */
    public String stateComment() {
        return this.stateComment;
    }

    /**
     * Set the stateComment value.
     *
     * @param stateComment the stateComment value to set
     * @return the SubscriptionUpdateParametersInner object itself.
     */
    public SubscriptionUpdateParametersInner withStateComment(String stateComment) {
        this.stateComment = stateComment;
        return this;
    }

}
