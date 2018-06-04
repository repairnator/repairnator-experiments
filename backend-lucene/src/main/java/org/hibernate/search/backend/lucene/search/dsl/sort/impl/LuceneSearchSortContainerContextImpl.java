/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.backend.lucene.search.dsl.sort.impl;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.backend.lucene.search.dsl.sort.LuceneSearchSortContainerContext;
import org.hibernate.search.backend.lucene.search.sort.impl.LuceneSearchSortCollector;
import org.hibernate.search.backend.lucene.search.sort.impl.LuceneSearchSortFactory;
import org.hibernate.search.search.dsl.sort.NonEmptySortContext;
import org.hibernate.search.search.dsl.sort.SearchSortContainerContext;
import org.hibernate.search.search.dsl.sort.spi.DelegatingSearchSortContainerContextImpl;
import org.hibernate.search.search.dsl.sort.spi.SearchSortDslContext;


public class LuceneSearchSortContainerContextImpl<N>
		extends DelegatingSearchSortContainerContextImpl<N>
		implements LuceneSearchSortContainerContext<N> {

	private final LuceneSearchSortFactory factory;

	private final SearchSortDslContext<N, LuceneSearchSortCollector> dslContext;

	public LuceneSearchSortContainerContextImpl(SearchSortContainerContext<N> delegate,
			LuceneSearchSortFactory factory,
			SearchSortDslContext<N, LuceneSearchSortCollector> dslContext) {
		super( delegate );
		this.factory = factory;
		this.dslContext = dslContext;
	}

	@Override
	public NonEmptySortContext<N> fromLuceneSortField(SortField luceneSortField) {
		dslContext.addContributor( factory.fromLuceneSortField( luceneSortField ) );
		return nonEmptyContext();
	}

	@Override
	public NonEmptySortContext<N> fromLuceneSort(Sort luceneSort) {
		dslContext.addContributor( factory.fromLuceneSort( luceneSort ) );
		return nonEmptyContext();
	}

	private NonEmptySortContext<N> nonEmptyContext() {
		return new NonEmptySortContext<N>() {
			@Override
			public SearchSortContainerContext<N> then() {
				return LuceneSearchSortContainerContextImpl.this;
			}

			@Override
			public N end() {
				return dslContext.getNextContext();
			}
		};
	}
}
