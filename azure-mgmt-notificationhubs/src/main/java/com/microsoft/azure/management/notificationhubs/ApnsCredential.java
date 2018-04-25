/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.notificationhubs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.rest.serializer.JsonFlatten;

/**
 * Description of a NotificationHub ApnsCredential.
 */
@JsonFlatten
public class ApnsCredential {
    /**
     * The APNS certificate.
     */
    @JsonProperty(value = "properties.apnsCertificate")
    private String apnsCertificate;

    /**
     * The certificate key.
     */
    @JsonProperty(value = "properties.certificateKey")
    private String certificateKey;

    /**
     * The endpoint of this credential.
     */
    @JsonProperty(value = "properties.endpoint")
    private String endpoint;

    /**
     * The Apns certificate Thumbprint.
     */
    @JsonProperty(value = "properties.thumbprint")
    private String thumbprint;

    /**
     * A 10-character key identifier (kid) key, obtained from your developer
     * account.
     */
    @JsonProperty(value = "properties.keyId")
    private String keyId;

    /**
     * The name of the application.
     */
    @JsonProperty(value = "properties.appName")
    private String appName;

    /**
     * The issuer (iss) registered claim key, whose value is your 10-character
     * Team ID, obtained from your developer account.
     */
    @JsonProperty(value = "properties.appId")
    private String appId;

    /**
     * Provider Authentication Token, obtained through your developer account.
     */
    @JsonProperty(value = "properties.token")
    private String token;

    /**
     * Get the apnsCertificate value.
     *
     * @return the apnsCertificate value
     */
    public String apnsCertificate() {
        return this.apnsCertificate;
    }

    /**
     * Set the apnsCertificate value.
     *
     * @param apnsCertificate the apnsCertificate value to set
     * @return the ApnsCredential object itself.
     */
    public ApnsCredential withApnsCertificate(String apnsCertificate) {
        this.apnsCertificate = apnsCertificate;
        return this;
    }

    /**
     * Get the certificateKey value.
     *
     * @return the certificateKey value
     */
    public String certificateKey() {
        return this.certificateKey;
    }

    /**
     * Set the certificateKey value.
     *
     * @param certificateKey the certificateKey value to set
     * @return the ApnsCredential object itself.
     */
    public ApnsCredential withCertificateKey(String certificateKey) {
        this.certificateKey = certificateKey;
        return this;
    }

    /**
     * Get the endpoint value.
     *
     * @return the endpoint value
     */
    public String endpoint() {
        return this.endpoint;
    }

    /**
     * Set the endpoint value.
     *
     * @param endpoint the endpoint value to set
     * @return the ApnsCredential object itself.
     */
    public ApnsCredential withEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    /**
     * Get the thumbprint value.
     *
     * @return the thumbprint value
     */
    public String thumbprint() {
        return this.thumbprint;
    }

    /**
     * Set the thumbprint value.
     *
     * @param thumbprint the thumbprint value to set
     * @return the ApnsCredential object itself.
     */
    public ApnsCredential withThumbprint(String thumbprint) {
        this.thumbprint = thumbprint;
        return this;
    }

    /**
     * Get the keyId value.
     *
     * @return the keyId value
     */
    public String keyId() {
        return this.keyId;
    }

    /**
     * Set the keyId value.
     *
     * @param keyId the keyId value to set
     * @return the ApnsCredential object itself.
     */
    public ApnsCredential withKeyId(String keyId) {
        this.keyId = keyId;
        return this;
    }

    /**
     * Get the appName value.
     *
     * @return the appName value
     */
    public String appName() {
        return this.appName;
    }

    /**
     * Set the appName value.
     *
     * @param appName the appName value to set
     * @return the ApnsCredential object itself.
     */
    public ApnsCredential withAppName(String appName) {
        this.appName = appName;
        return this;
    }

    /**
     * Get the appId value.
     *
     * @return the appId value
     */
    public String appId() {
        return this.appId;
    }

    /**
     * Set the appId value.
     *
     * @param appId the appId value to set
     * @return the ApnsCredential object itself.
     */
    public ApnsCredential withAppId(String appId) {
        this.appId = appId;
        return this;
    }

    /**
     * Get the token value.
     *
     * @return the token value
     */
    public String token() {
        return this.token;
    }

    /**
     * Set the token value.
     *
     * @param token the token value to set
     * @return the ApnsCredential object itself.
     */
    public ApnsCredential withToken(String token) {
        this.token = token;
        return this;
    }

}
