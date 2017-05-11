/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.processors.gcp.credentials.factory;

import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.processor.util.StandardValidators;

/**
 * Shared definitions of properties that specify various GCP credentials.
 */
public final class CredentialPropertyDescriptors {

    private CredentialPropertyDescriptors() {}

    /**
     * Specifies use of Application Default Credentials
     *
     * @see <a href="https://developers.google.com/identity/protocols/application-default-credentials">
     *     Google Application Default Credentials
     *     </a>
     */
    public static final PropertyDescriptor USE_APPLICATION_DEFAULT_CREDENTIALS = new PropertyDescriptor.Builder()
            .name("application-default-credentials")
            .displayName("Use Application Default Credentials")
            .expressionLanguageSupported(false)
            .required(false)
            .addValidator(StandardValidators.BOOLEAN_VALIDATOR)
            .sensitive(false)
            .allowableValues("true", "false")
            .defaultValue("false")
            .description("If true, uses Google Application Default Credentials, which checks the " +
                    "GOOGLE_APPLICATION_CREDENTIALS environment variable for a filepath to a service account JSON " +
                    "key, the config generated by the gcloud sdk, the App Engine service account, and the Compute" +
                    " Engine service account.")
            .build();

    public static final PropertyDescriptor USE_COMPUTE_ENGINE_CREDENTIALS = new PropertyDescriptor.Builder()
            .name("compute-engine-credentials")
            .displayName("Use Compute Engine Credentials")
            .expressionLanguageSupported(false)
            .required(false)
            .addValidator(StandardValidators.BOOLEAN_VALIDATOR)
            .sensitive(false)
            .allowableValues("true", "false")
            .defaultValue("false")
            .description("If true, uses Google Compute Engine Credentials of the Compute Engine VM Instance " +
                    "which NiFi is running on.")
            .build();

    /**
     * Specifies use of Service Account Credentials
     *
     * @see <a href="https://cloud.google.com/iam/docs/service-accounts">
     *     Google Service Accounts
     *     </a>
     */
    public static final PropertyDescriptor SERVICE_ACCOUNT_JSON_FILE = new PropertyDescriptor.Builder()
            .name("service-account-json-file")
            .displayName("Service Account JSON File")
            .expressionLanguageSupported(false)
            .required(false)
            .addValidator(StandardValidators.FILE_EXISTS_VALIDATOR)
            .description("Path to a file containing a Service Account key file in JSON format.")
            .build();

    public static final PropertyDescriptor SERVICE_ACCOUNT_JSON = new PropertyDescriptor.Builder()
            .name("service-account-json")
            .displayName("Service Account JSON")
            .expressionLanguageSupported(true)
            .required(false)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .description("The raw JSON containing a Service Account keyfile.")
            .sensitive(true)
            .build();
}
