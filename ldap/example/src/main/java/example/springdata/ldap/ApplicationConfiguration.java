/*
 * Copyright 2017-2018 the original author or authors.
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
package example.springdata.ldap;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Test configuration to spin up a in-memory LDAP server (see {@code application.properties}). Also enables Spring Data
 * repositories for LDAP.
 *
 * @author Mark Paluch
 */
@SpringBootApplication
class ApplicationConfiguration {}
