/*
 *  Copyright 2018 original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.springframework.cloud.gcp.autoconfigure.logging.extractors;

import javax.servlet.http.HttpServletRequest;

/**
 * Extracts trace IDs from HTTP requests using the x-cloud-trace-context header.
 *
 * @author Chengyuan Zhao
 */
public class XCloudTraceIdExtractor implements TraceIdExtractor {

	public static final String X_CLOUD_TRACE_HEADER = "x-cloud-trace-context";

	@Override
	public String extractTraceIdFromRequest(HttpServletRequest req) {
		String traceId = req.getHeader(X_CLOUD_TRACE_HEADER);

		if (traceId != null) {
			int slash = traceId.indexOf('/');
			if (slash >= 0) {
				traceId = traceId.substring(0, slash);
			}
		}
		return traceId;
	}
}
