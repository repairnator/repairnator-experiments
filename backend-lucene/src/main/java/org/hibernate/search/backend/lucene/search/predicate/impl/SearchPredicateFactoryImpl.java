/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.backend.lucene.search.predicate.impl;

import java.lang.invoke.MethodHandles;

import org.apache.lucene.search.Query;
import org.hibernate.search.backend.lucene.logging.impl.Log;
import org.hibernate.search.backend.lucene.search.impl.LuceneSearchQueryElementCollector;
import org.hibernate.search.backend.lucene.search.impl.LuceneSearchTargetModel;
import org.hibernate.search.search.SearchPredicate;
import org.hibernate.search.search.predicate.spi.BooleanJunctionPredicateBuilder;
import org.hibernate.search.search.predicate.spi.MatchAllPredicateBuilder;
import org.hibernate.search.search.predicate.spi.MatchPredicateBuilder;
import org.hibernate.search.search.predicate.spi.NestedPredicateBuilder;
import org.hibernate.search.search.predicate.spi.RangePredicateBuilder;
import org.hibernate.search.search.predicate.spi.SearchPredicateContributor;
import org.hibernate.search.search.predicate.spi.SpatialWithinBoundingBoxPredicateBuilder;
import org.hibernate.search.search.predicate.spi.SpatialWithinCirclePredicateBuilder;
import org.hibernate.search.search.predicate.spi.SpatialWithinPolygonPredicateBuilder;
import org.hibernate.search.util.impl.common.LoggerFactory;

/**
 * @author Guillaume Smet
 */
public class SearchPredicateFactoryImpl implements LuceneSearchPredicateFactory {

	private static final Log log = LoggerFactory.make( Log.class, MethodHandles.lookup() );

	private final LuceneSearchTargetModel searchTargetModel;

	public SearchPredicateFactoryImpl(LuceneSearchTargetModel searchTargetModel) {
		this.searchTargetModel = searchTargetModel;
	}

	@Override
	public SearchPredicate toSearchPredicate(SearchPredicateContributor<LuceneSearchPredicateCollector> contributor) {
		LuceneSearchQueryElementCollector collector = new LuceneSearchQueryElementCollector();
		contributor.contribute( collector );
		return new LuceneSearchPredicate( collector.toLuceneQueryPredicate() );
	}

	@Override
	public SearchPredicateContributor<LuceneSearchPredicateCollector> toContributor(SearchPredicate predicate) {
		if ( !( predicate instanceof LuceneSearchPredicate ) ) {
			throw log.cannotMixLuceneSearchQueryWithOtherPredicates( predicate );
		}
		return (LuceneSearchPredicate) predicate;
	}

	@Override
	public MatchAllPredicateBuilder<LuceneSearchPredicateCollector> matchAll() {
		return new MatchAllPredicateBuilderImpl();
	}

	@Override
	public BooleanJunctionPredicateBuilder<LuceneSearchPredicateCollector> bool() {
		return new BooleanJunctionPredicateBuilderImpl();
	}

	@Override
	public MatchPredicateBuilder<LuceneSearchPredicateCollector> match(String absoluteFieldPath) {
		return searchTargetModel.getSchemaNode( absoluteFieldPath ).getPredicateBuilderFactory().createMatchPredicateBuilder( absoluteFieldPath );
	}

	@Override
	public RangePredicateBuilder<LuceneSearchPredicateCollector> range(String absoluteFieldPath) {
		return searchTargetModel.getSchemaNode( absoluteFieldPath ).getPredicateBuilderFactory().createRangePredicateBuilder( absoluteFieldPath );
	}

	@Override
	public SpatialWithinCirclePredicateBuilder<LuceneSearchPredicateCollector> spatialWithinCircle(String absoluteFieldPath) {
		return searchTargetModel.getSchemaNode( absoluteFieldPath ).getPredicateBuilderFactory().createSpatialWithinCirclePredicateBuilder( absoluteFieldPath );
	}

	@Override
	public SpatialWithinPolygonPredicateBuilder<LuceneSearchPredicateCollector> spatialWithinPolygon(String absoluteFieldPath) {
		return searchTargetModel.getSchemaNode( absoluteFieldPath ).getPredicateBuilderFactory().createSpatialWithinPolygonPredicateBuilder( absoluteFieldPath );
	}

	@Override
	public SpatialWithinBoundingBoxPredicateBuilder<LuceneSearchPredicateCollector> spatialWithinBoundingBox(String absoluteFieldPath) {
		return searchTargetModel.getSchemaNode( absoluteFieldPath ).getPredicateBuilderFactory()
				.createSpatialWithinBoundingBoxPredicateBuilder( absoluteFieldPath );
	}

	@Override
	public NestedPredicateBuilder<LuceneSearchPredicateCollector> nested(String absoluteFieldPath) {
		searchTargetModel.checkNestedField( absoluteFieldPath );
		return new NestedPredicateBuilderImpl( absoluteFieldPath );
	}

	@Override
	public SearchPredicateContributor<LuceneSearchPredicateCollector> fromLuceneQuery(Query query) {
		return new UserProvidedLuceneQueryPredicateContributor( query );
	}

}
