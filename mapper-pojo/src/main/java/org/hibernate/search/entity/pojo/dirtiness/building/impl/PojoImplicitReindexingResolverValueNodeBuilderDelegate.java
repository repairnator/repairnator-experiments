/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.entity.pojo.dirtiness.building.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.hibernate.search.entity.pojo.dirtiness.impl.PojoImplicitReindexingResolver;
import org.hibernate.search.entity.pojo.model.path.impl.BoundPojoModelPathValueNode;
import org.hibernate.search.entity.pojo.model.spi.PojoRawTypeModel;
import org.hibernate.search.entity.pojo.model.spi.PojoTypeModel;
import org.hibernate.search.util.AssertionFailure;

class PojoImplicitReindexingResolverValueNodeBuilderDelegate<V> {

	private final BoundPojoModelPathValueNode<?, ?, V> modelPath;
	private final PojoImplicitReindexingResolverBuildingHelper buildingHelper;

	private PojoImplicitReindexingResolverOriginalTypeNodeBuilder<V> typeNodeBuilder;
	// Use a LinkedHashMap for deterministic iteration
	private final Map<PojoRawTypeModel<?>, PojoImplicitReindexingResolverCastedTypeNodeBuilder<V, ?>>
			castedTypeNodeBuilders = new LinkedHashMap<>();

	PojoImplicitReindexingResolverValueNodeBuilderDelegate(BoundPojoModelPathValueNode<?, ?, V> modelPath,
			PojoImplicitReindexingResolverBuildingHelper buildingHelper) {
		this.modelPath = modelPath;
		this.buildingHelper = buildingHelper;
	}

	PojoTypeModel<V> getTypeModel() {
		return modelPath.type().getTypeModel();
	}

	<U> AbstractPojoImplicitReindexingResolverTypeNodeBuilder<V, ?> type(PojoRawTypeModel<U> targetTypeModel) {
		PojoRawTypeModel<? super V> valueRawTypeModel = getTypeModel().getRawType();
		if ( valueRawTypeModel.isSubTypeOf( targetTypeModel ) ) {
			// No need to cast, we're already satisfying the requirements
			return type();
		}
		else if ( targetTypeModel.isSubTypeOf( valueRawTypeModel ) ) {
			// Need to downcast
			return getOrCreateCastedTypeNodeBuilder( targetTypeModel );
		}
		else {
			/*
			 * Types are incompatible; this problem should have already been detected and reported
			 * by the caller, so we just throw an assertion failure here.
			 */
			throw new AssertionFailure(
					"Error while building the automatic reindexing resolver at path " + modelPath
					+ ": attempt to convert a reindexing resolver builder to an incorrect type; "
					+ " got " + targetTypeModel + ", but a subtype of " + valueRawTypeModel
					+ " was expected."
					+ " This is very probably a bug in Hibernate Search, please report it."
			);
		}
	}

	PojoImplicitReindexingResolverOriginalTypeNodeBuilder<V> type() {
		if ( typeNodeBuilder == null ) {
			typeNodeBuilder = new PojoImplicitReindexingResolverOriginalTypeNodeBuilder<>( modelPath.type(), buildingHelper );
		}
		return typeNodeBuilder;
	}

	Collection<PojoImplicitReindexingResolver<V>> buildTypeNodes() {
		Collection<PojoImplicitReindexingResolver<V>> immutableTypeNodes = new ArrayList<>();
		if ( typeNodeBuilder != null ) {
			typeNodeBuilder.build().ifPresent( immutableTypeNodes::add );
		}
		castedTypeNodeBuilders.values().stream()
				.map( PojoImplicitReindexingResolverCastedTypeNodeBuilder::build )
				.filter( Optional::isPresent )
				.map( Optional::get )
				.forEach( immutableTypeNodes::add );

		return immutableTypeNodes;
	}

	@SuppressWarnings("unchecked") // We know builders have this exact type, by construction
	private <U> PojoImplicitReindexingResolverCastedTypeNodeBuilder<V, U> getOrCreateCastedTypeNodeBuilder(
			PojoRawTypeModel<U> targetTypeModel) {
		return (PojoImplicitReindexingResolverCastedTypeNodeBuilder<V, U>)
				castedTypeNodeBuilders.computeIfAbsent( targetTypeModel, this::createCastedTypeNodeBuilder );
	}

	private <U> PojoImplicitReindexingResolverCastedTypeNodeBuilder<V, U> createCastedTypeNodeBuilder(
			PojoRawTypeModel<U> targetTypeModel) {
		return new PojoImplicitReindexingResolverCastedTypeNodeBuilder<>(
				modelPath.castedType( targetTypeModel ), buildingHelper
		);
	}

}
