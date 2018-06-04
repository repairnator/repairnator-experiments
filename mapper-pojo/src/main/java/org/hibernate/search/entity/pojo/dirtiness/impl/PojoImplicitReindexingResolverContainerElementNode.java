/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.entity.pojo.dirtiness.impl;

import java.util.Collection;
import java.util.stream.Stream;

import org.hibernate.search.entity.pojo.extractor.ContainerValueExtractor;
import org.hibernate.search.entity.pojo.model.spi.PojoRuntimeIntrospector;
import org.hibernate.search.util.impl.common.ToStringTreeBuilder;

/**
 * A {@link PojoImplicitReindexingResolver} node dealing with a specific container type,
 * extracting values from the container then applying nested resolvers to the values.
 * <p>
 * This node will only delegate to nested nodes for deeper resolution,
 * and will never contribute entities to reindex directly.
 * At the time of writing, nested nodes are always type nodes,
 * but we might allow other nodes in the future for optimization purposes.
 *
 * @param <C> The container type received as input, for instance {@code Map<String, Collection<MyEntityType>>}.
 * @param <V> The extracted value type, for instance {@code MyEntityType}.
 */
public class PojoImplicitReindexingResolverContainerElementNode<C, V> extends PojoImplicitReindexingResolver<C> {

	private final ContainerValueExtractor<C, V> extractor;
	private final Collection<PojoImplicitReindexingResolver<V>> nestedNodes;

	public PojoImplicitReindexingResolverContainerElementNode(ContainerValueExtractor<C, V> extractor,
			Collection<PojoImplicitReindexingResolver<V>> nestedNodes) {
		this.extractor = extractor;
		this.nestedNodes = nestedNodes;
	}

	@Override
	public void appendTo(ToStringTreeBuilder builder) {
		builder.attribute( "class", getClass().getSimpleName() );
		builder.attribute( "extractor", extractor );
		builder.startList( "nestedNodes" );
		for ( PojoImplicitReindexingResolver<?> nestedNode : nestedNodes ) {
			builder.value( nestedNode );
		}
		builder.endList();
	}

	@Override
	public void resolveEntitiesToReindex(PojoReindexingCollector collector,
			PojoRuntimeIntrospector runtimeIntrospector, C dirty) {
		try ( Stream<V> stream = extractor.extract( dirty ) ) {
			stream.forEach( containerElement -> resolveEntitiesToReindexForContainerElement(
					collector, runtimeIntrospector, containerElement
			) );
		}
	}

	private void resolveEntitiesToReindexForContainerElement(PojoReindexingCollector collector,
			PojoRuntimeIntrospector runtimeIntrospector, V containerElement) {
		if ( containerElement != null ) {
			for ( PojoImplicitReindexingResolver<V> node : nestedNodes ) {
				node.resolveEntitiesToReindex( collector, runtimeIntrospector, containerElement );
			}
		}
	}
}
