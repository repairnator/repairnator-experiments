/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.entity.pojo.dirtiness.building.impl;

import java.util.Collection;

import org.hibernate.search.entity.pojo.dirtiness.impl.PojoImplicitReindexingResolverNode;
import org.hibernate.search.entity.pojo.dirtiness.impl.PojoImplicitReindexingResolverCastedTypeNode;
import org.hibernate.search.entity.pojo.model.path.impl.BoundPojoModelPathCastedTypeNode;

class PojoImplicitReindexingResolverCastedTypeNodeBuilder<T, U>
		extends AbstractPojoImplicitReindexingResolverTypeNodeBuilder<T, U> {

	PojoImplicitReindexingResolverCastedTypeNodeBuilder(BoundPojoModelPathCastedTypeNode<T, U> modelPath,
			PojoImplicitReindexingResolverBuildingHelper buildingHelper) {
		super( modelPath, buildingHelper );
	}

	@Override
	<S> PojoImplicitReindexingResolverNode<T, S> doBuild(
			Collection<PojoImplicitReindexingResolverNode<? super U, S>> immutableNestedNodes) {
		return new PojoImplicitReindexingResolverCastedTypeNode<>(
				getTypeModel().getRawType().getCaster(), immutableNestedNodes
		);
	}
}
