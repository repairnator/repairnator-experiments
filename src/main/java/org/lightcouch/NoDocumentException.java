/*
 * Copyright (C) 2011 lightcouch.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lightcouch;

/**
 * Thrown when a requested document is not found.
 * @since 0.0.2
 * @author Ahmed Yehia
 */
public class NoDocumentException extends CouchDbException {

	private static final long serialVersionUID = 1L;
	
	public NoDocumentException(String message) {
		super(message);
	}
	
	public NoDocumentException(Throwable cause) {
		super(cause);
	}
	
	public NoDocumentException(String message, Throwable cause) {
		super(message, cause);
	}
}
