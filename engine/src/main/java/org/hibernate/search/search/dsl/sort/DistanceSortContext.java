/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.search.dsl.sort;

/**
 * The context used when defining a field sort.
 *
 * @param <N> The type of the end context (returned by {@link DistanceSortContext#end()}).
 */
public interface DistanceSortContext<N>
		extends NonEmptySortContext<N>, SortOrderContext<DistanceSortContext<N>> {

}
