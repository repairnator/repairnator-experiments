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

package org.springframework.cloud.gcp.data.spanner.repository.support;

import com.google.cloud.spanner.Key;
import org.junit.Before;
import org.junit.Test;

import org.springframework.cloud.gcp.data.spanner.core.SpannerTemplate;
import org.springframework.cloud.gcp.data.spanner.core.mapping.SpannerMappingContext;
import org.springframework.cloud.gcp.data.spanner.repository.SpannerRepository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author Chengyuan Zhao
 */
public class SpannerRepositoryFactoryBeanTests {

	private SpannerRepositoryFactoryBean<SpannerRepository<Object, Key>, Object, Key>
			spannerRepositoryFactoryBean;

	private SpannerMappingContext spannerMappingContext;

	private SpannerTemplate spannerTemplate;

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() {
		this.spannerMappingContext = new SpannerMappingContext();
		this.spannerTemplate = mock(SpannerTemplate.class);
		this.spannerRepositoryFactoryBean = new SpannerRepositoryFactoryBean(
				SpannerRepository.class);
		this.spannerRepositoryFactoryBean
				.setSpannerMappingContext(this.spannerMappingContext);
		this.spannerRepositoryFactoryBean.setSpannerTemplate(this.spannerTemplate);
	}

	@Test
	public void createRepositoryFactoryTest() {
		RepositoryFactorySupport factory = this.spannerRepositoryFactoryBean
				.createRepositoryFactory();
		assertEquals(SpannerRepositoryFactory.class, factory.getClass());
	}
}
