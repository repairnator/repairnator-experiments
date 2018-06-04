/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.test.bridge;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.bridge.MetadataProvidingFieldBridge;
import org.hibernate.search.testsupport.junit.SkipOnElasticsearch;
import org.junit.experimental.categories.Category;

/**
 * Test that custom fields created using a {@link MetadataProvidingFieldBridge} use the expected defaults.
 *
 * @author Davide D'Alto
 */
@Category(SkipOnElasticsearch.class) // There is a similar test extending the same class for Elasticsearch
public class CheckCustomFieldDefaultsTest extends CheckCustomFieldDefaultAnalyzer {

	@Override
	protected Object entity(String id, String code, String result) {
		Experiment experiment = new Experiment();
		experiment.id = id;
		experiment.subject = code;
		experiment.result = result;
		return experiment;
	}

	@Override
	protected Class<?> getEntityType() {
		return Experiment.class;
	}

	@Indexed
	private static class Experiment {
		@DocumentId
		@FieldBridge(impl = AdditionalFieldBridge.class)
		public String id;

		@Field(analyze = Analyze.NO)
		@FieldBridge(impl = AdditionalFieldBridge.class)
		public String subject;

		@Field(analyze = Analyze.YES)
		@FieldBridge(impl = AdditionalFieldBridge.class)
		@Analyzer(impl = WhitespaceAnalyzer.class)
		public String result;
	}
}
