/*
 * #%L
 * OAuth API
 * %%
 * Copyright (C) 2009 - 2013 Sakai Foundation
 * %%
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *             http://opensource.org/licenses/ecl2
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.sakaiproject.oauth.exception;

/**
 * Exception thrown when a consumer can't be used, because it's nonexistant or disabled.
 *
 * @author Colin Hebert
 */
public class InvalidConsumerException extends OAuthException {
    public InvalidConsumerException() {
    }

    public InvalidConsumerException(Throwable cause) {
        super(cause);
    }

    public InvalidConsumerException(String message) {
        super(message);
    }

    public InvalidConsumerException(String message, Throwable cause) {
        super(message, cause);
    }
}
