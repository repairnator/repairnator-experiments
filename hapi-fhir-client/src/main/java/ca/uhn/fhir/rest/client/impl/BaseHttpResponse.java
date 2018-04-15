package ca.uhn.fhir.rest.client.impl;

/*-
 * #%L
 * HAPI FHIR - Client Framework
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.util.StopWatch;

public abstract class BaseHttpResponse implements IHttpResponse {
	private final StopWatch myRequestStopWatch;

	public BaseHttpResponse(StopWatch theRequestStopWatch) {
		myRequestStopWatch = theRequestStopWatch;
	}

	@Override
	public StopWatch getRequestStopWatch() {
		return myRequestStopWatch;
	}
}
