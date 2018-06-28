/*
 * Copyright 2018 the original author or authors.
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
package org.springframework.data.mongodb.core.validation;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.SerializationUtils;
import org.springframework.util.Assert;

/**
 * {@link Validator} implementation based on {@link CriteriaDefinition query expressions}.
 *
 * @author Andreas Zink
 * @author Christoph Strobl
 * @since 2.1
 * @see Criteria
 * @see <a href="https://docs.mongodb.com/manual/core/schema-validation/#query-expressions">Schema Validation</a>
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
class CriteriaValidator implements Validator {

	private final CriteriaDefinition criteria;

	/**
	 * Creates a new {@link Validator} object, which is basically setup of query operators, based on a
	 * {@link CriteriaDefinition} instance.
	 *
	 * @param criteria the criteria to build the {@code validator} from. Must not be {@literal null}.
	 * @return new instance of {@link CriteriaValidator}.
	 * @throws IllegalArgumentException when criteria is {@literal null}.
	 */
	static CriteriaValidator of(CriteriaDefinition criteria) {

		Assert.notNull(criteria, "Criteria must not be null!");

		return new CriteriaValidator(criteria);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.mongodb.core.validation.Validator#toDocument()
	 */
	@Override
	public Document toDocument() {
		return criteria.getCriteriaObject();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return SerializationUtils.serializeToJsonSafely(toDocument());
	}
}
