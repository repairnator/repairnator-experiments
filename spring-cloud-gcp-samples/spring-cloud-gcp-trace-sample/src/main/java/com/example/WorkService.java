/*
 *  Copyright 2017 original author or authors.
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

package com.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkService {
	private static final Log LOGGER = LogFactory.getLog(WorkService.class);

	private final RestTemplate restTemplate;

	public WorkService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@NewSpan
	public void busyWork() {
		LOGGER.info("starting busy work");
		for (int i = 0; i < 3; i++) {
			this.restTemplate.getForObject("http://localhost:8080/meet", String.class);
		}
		LOGGER.info("finished busy work");
	}
}
