/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.backend.lucene.search.predicate.impl;

import org.hibernate.search.search.predicate.spi.MatchAllPredicateBuilder;

import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;

/**
 * @author Guillaume Smet
 */
class MatchAllPredicateBuilderImpl extends AbstractSearchPredicateBuilder
		implements MatchAllPredicateBuilder<LuceneSearchPredicateBuilder> {

	@Override
	protected Query doBuild(LuceneSearchPredicateContext context) {
		return new MatchAllDocsQuery();
	}
}
