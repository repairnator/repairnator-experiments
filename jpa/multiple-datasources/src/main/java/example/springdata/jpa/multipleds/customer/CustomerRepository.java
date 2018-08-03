/*
 * Copyright 2015-2018 the original author or authors.
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
package example.springdata.jpa.multipleds.customer;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data repository to manage {@link Customer}s.
 *
 * @author Oliver Gierke
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	Optional<Customer> findByLastname(String lastname);
}
