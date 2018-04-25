/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.appservice.implementation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.rest.serializer.JsonFlatten;
import com.microsoft.azure.management.appservice.ProxyOnlyResource;

/**
 * User crendentials used for publishing activity.
 */
@JsonFlatten
public class UserInner extends ProxyOnlyResource {
    /**
     * Username.
     */
    @JsonProperty(value = "properties.name")
    private String userName;

    /**
     * Username used for publishing.
     */
    @JsonProperty(value = "properties.publishingUserName", required = true)
    private String publishingUserName;

    /**
     * Password used for publishing.
     */
    @JsonProperty(value = "properties.publishingPassword")
    private String publishingPassword;

    /**
     * Password hash used for publishing.
     */
    @JsonProperty(value = "properties.publishingPasswordHash")
    private String publishingPasswordHash;

    /**
     * Password hash salt used for publishing.
     */
    @JsonProperty(value = "properties.publishingPasswordHashSalt")
    private String publishingPasswordHashSalt;

    /**
     * Get the userName value.
     *
     * @return the userName value
     */
    public String userName() {
        return this.userName;
    }

    /**
     * Set the userName value.
     *
     * @param userName the userName value to set
     * @return the UserInner object itself.
     */
    public UserInner withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    /**
     * Get the publishingUserName value.
     *
     * @return the publishingUserName value
     */
    public String publishingUserName() {
        return this.publishingUserName;
    }

    /**
     * Set the publishingUserName value.
     *
     * @param publishingUserName the publishingUserName value to set
     * @return the UserInner object itself.
     */
    public UserInner withPublishingUserName(String publishingUserName) {
        this.publishingUserName = publishingUserName;
        return this;
    }

    /**
     * Get the publishingPassword value.
     *
     * @return the publishingPassword value
     */
    public String publishingPassword() {
        return this.publishingPassword;
    }

    /**
     * Set the publishingPassword value.
     *
     * @param publishingPassword the publishingPassword value to set
     * @return the UserInner object itself.
     */
    public UserInner withPublishingPassword(String publishingPassword) {
        this.publishingPassword = publishingPassword;
        return this;
    }

    /**
     * Get the publishingPasswordHash value.
     *
     * @return the publishingPasswordHash value
     */
    public String publishingPasswordHash() {
        return this.publishingPasswordHash;
    }

    /**
     * Set the publishingPasswordHash value.
     *
     * @param publishingPasswordHash the publishingPasswordHash value to set
     * @return the UserInner object itself.
     */
    public UserInner withPublishingPasswordHash(String publishingPasswordHash) {
        this.publishingPasswordHash = publishingPasswordHash;
        return this;
    }

    /**
     * Get the publishingPasswordHashSalt value.
     *
     * @return the publishingPasswordHashSalt value
     */
    public String publishingPasswordHashSalt() {
        return this.publishingPasswordHashSalt;
    }

    /**
     * Set the publishingPasswordHashSalt value.
     *
     * @param publishingPasswordHashSalt the publishingPasswordHashSalt value to set
     * @return the UserInner object itself.
     */
    public UserInner withPublishingPasswordHashSalt(String publishingPasswordHashSalt) {
        this.publishingPasswordHashSalt = publishingPasswordHashSalt;
        return this;
    }

}
