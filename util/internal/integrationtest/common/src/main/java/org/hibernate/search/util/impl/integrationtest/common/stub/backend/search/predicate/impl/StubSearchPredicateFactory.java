/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.util.impl.integrationtest.common.stub.backend.search.predicate.impl;

import org.hibernate.search.util.impl.integrationtest.common.stub.backend.search.StubQueryElementCollector;
import org.hibernate.search.search.SearchPredicate;
import org.hibernate.search.search.predicate.spi.MatchAllPredicateBuilder;
import org.hibernate.search.search.predicate.spi.BooleanJunctionPredicateBuilder;
import org.hibernate.search.search.predicate.spi.MatchPredicateBuilder;
import org.hibernate.search.search.predicate.spi.NestedPredicateBuilder;
import org.hibernate.search.search.predicate.spi.RangePredicateBuilder;
import org.hibernate.search.search.predicate.spi.SearchPredicateFactory;
import org.hibernate.search.search.predicate.spi.SpatialWithinBoundingBoxPredicateBuilder;
import org.hibernate.search.search.predicate.spi.SpatialWithinCirclePredicateBuilder;
import org.hibernate.search.search.predicate.spi.SpatialWithinPolygonPredicateBuilder;

public class StubSearchPredicateFactory implements SearchPredicateFactory<StubQueryElementCollector, StubPredicateBuilder> {

	@Override
	public SearchPredicate toSearchPredicate(StubPredicateBuilder builder) {
		return new StubSearchPredicate( builder );
	}

	@Override
	public StubPredicateBuilder toImplementation(SearchPredicate predicate) {
		return ((StubSearchPredicate) predicate).get();
	}

	@Override
	public void contribute(StubQueryElementCollector collector, StubPredicateBuilder builder) {
		builder.simulateBuild();
		collector.simulateCollectCall();
	}

	@Override
	public MatchAllPredicateBuilder<StubPredicateBuilder> matchAll() {
		return new StubPredicateBuilder();
	}

	@Override
	public BooleanJunctionPredicateBuilder<StubPredicateBuilder> bool() {
		return new StubPredicateBuilder();
	}

	@Override
	public MatchPredicateBuilder<StubPredicateBuilder> match(String absoluteFieldPath) {
		return new StubPredicateBuilder();
	}

	@Override
	public RangePredicateBuilder<StubPredicateBuilder> range(String absoluteFieldPath) {
		return new StubPredicateBuilder();
	}

	@Override
	public SpatialWithinCirclePredicateBuilder<StubPredicateBuilder> spatialWithinCircle(String absoluteFieldPath) {
		return new StubPredicateBuilder();
	}

	@Override
	public SpatialWithinPolygonPredicateBuilder<StubPredicateBuilder> spatialWithinPolygon(String absoluteFieldPath) {
		return new StubPredicateBuilder();
	}

	@Override
	public SpatialWithinBoundingBoxPredicateBuilder<StubPredicateBuilder> spatialWithinBoundingBox(String absoluteFieldPath) {
		return new StubPredicateBuilder();
	}

	@Override
	public NestedPredicateBuilder<StubPredicateBuilder> nested(String absoluteFieldPath) {
		return new StubPredicateBuilder();
	}
}
