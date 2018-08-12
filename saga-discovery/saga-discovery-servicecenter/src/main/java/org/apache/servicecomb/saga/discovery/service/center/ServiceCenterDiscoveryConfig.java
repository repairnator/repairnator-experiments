/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.saga.discovery.service.center;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.apache.servicecomb.saga.transports.HttpClientTransportConfig;
import org.apache.servicecomb.saga.transports.RestTransport;
import org.apache.servicecomb.saga.transports.resttemplate.RestTemplateTransport;
import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;

@EnableServiceComb
@Profile("servicecomb")
@Configuration
@AutoConfigureBefore(HttpClientTransportConfig.class)
public class ServiceCenterDiscoveryConfig {

  static final String PROTOCOL = "cse://";

  @Bean
  RestTransport restTransport() {
    return new RestTemplateTransport(RestTemplateBuilder.create(), PROTOCOL);
  }
}
