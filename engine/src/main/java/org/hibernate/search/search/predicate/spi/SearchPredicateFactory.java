/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.search.predicate.spi;

import org.hibernate.search.search.SearchPredicate;

/**
 * A factory for search predicates.
 * <p>
 * This is the main entry point for the engine
 * to ask the backend to build search predicates.
 *
 * @param <C> The type of predicate collector the builders contributor will contribute to.
 * This type is backend-specific. See {@link SearchPredicateBuilder#contribute(Object)}
 */
public interface SearchPredicateFactory<C> {

	SearchPredicate toSearchPredicate(SearchPredicateContributor<C> contributor);

	SearchPredicateContributor<C> toContributor(SearchPredicate predicate);

	MatchAllPredicateBuilder<C> matchAll();

	BooleanJunctionPredicateBuilder<C> bool();

	MatchPredicateBuilder<C> match(String absoluteFieldPath);

	RangePredicateBuilder<C> range(String absoluteFieldPath);

	NestedPredicateBuilder<C> nested(String absoluteFieldPath);

	SpatialWithinCirclePredicateBuilder<C> spatialWithinCircle(String absoluteFieldPath);

	SpatialWithinPolygonPredicateBuilder<C> spatialWithinPolygon(String absoluteFieldPath);

	SpatialWithinBoundingBoxPredicateBuilder<C> spatialWithinBoundingBox(String absoluteFieldPath);

}
