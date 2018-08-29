/*
 * #%L
 * ACS AEM Commons Bundle
 * %%
 * Copyright (C) 2013 Adobe
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.adobe.acs.commons.email;

import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import aQute.bnd.annotation.ProviderType;

/**
 * A service interface for sending a generic template based Email Notification.
 * 
 */
@ProviderType
public interface EmailService {

    /**
     * Construct an email based on a template and send it to one or more
     * recipients.
     * 
     * @param templatePath Absolute path of the template used to send the email.
     * @param emailParams Replacement variable map to be injected in the template
     * @param recipients recipient email addresses
     * 
     * @return failureList containing list recipient's InternetAddresses for which email sent failed
     */
    List<InternetAddress> sendEmail(String templatePath, Map<String, String> emailParams,
        InternetAddress... recipients);

    /**
     * Construct an email based on a template and send it to one or more
     * recipients.
     * 
     * @param templatePath Absolute path of the template used to send the email.
     * @param emailParams Replacement variable map to be injected in the template
     * @param recipients recipient email addresses. Invalid email addresses are skipped.
     * 
     * @return failureList containing list recipient's String addresses for which email sent failed
     */
    List<String> sendEmail(String templatePath, Map<String, String> emailParams, String... recipients);
}
