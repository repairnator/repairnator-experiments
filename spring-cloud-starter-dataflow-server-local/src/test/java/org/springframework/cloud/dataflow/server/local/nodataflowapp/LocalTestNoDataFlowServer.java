/*
 * Copyright 2016 the original author or authors.
 *
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
 */

package org.springframework.cloud.dataflow.server.local.nodataflowapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Bootstrap class for dummy spring boot app having no enabled dataflow server configs.
 * <p>
 * Multiple SpringBootApplication's needs to be in their own directories due to component
 * scanning.
 *
 * @author Janne Valkealahti
 */
@SpringBootApplication
public class LocalTestNoDataFlowServer {

	public static void main(String[] args) {
		SpringApplication.run(LocalTestNoDataFlowServer.class, args);
	}
}
