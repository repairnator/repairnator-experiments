/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.backend.index.spi;

import org.hibernate.search.search.SearchPredicate;
import org.hibernate.search.search.SearchSort;
import org.hibernate.search.search.dsl.predicate.SearchPredicateContainerContext;
import org.hibernate.search.search.dsl.predicate.impl.SearchTargetPredicateRootContext;
import org.hibernate.search.search.dsl.sort.SearchSortContainerContext;
import org.hibernate.search.search.dsl.sort.impl.SearchTargetSortRootContext;
import org.hibernate.search.search.dsl.spi.SearchTargetContext;

public abstract class IndexSearchTargetBase implements IndexSearchTarget {

	@Override
	public SearchPredicateContainerContext<SearchPredicate> predicate() {
		return new SearchTargetPredicateRootContext<>( getSearchTargetContext().getSearchPredicateFactory() );
	}

	@Override
	public SearchSortContainerContext<SearchSort> sort() {
		return new SearchTargetSortRootContext<>( getSearchTargetContext().getSearchSortFactory() );
	}

	protected abstract SearchTargetContext<?> getSearchTargetContext();

}
