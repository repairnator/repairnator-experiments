/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.entity.pojo.mapping.definition.programmatic.impl;

import org.hibernate.search.engine.spi.BeanReference;
import org.hibernate.search.engine.spi.ImmutableBeanReference;
import org.hibernate.search.entity.pojo.bridge.impl.BeanResolverBridgeBuilder;
import org.hibernate.search.entity.pojo.bridge.mapping.BridgeBuilder;
import org.hibernate.search.entity.pojo.bridge.IdentifierBridge;
import org.hibernate.search.entity.pojo.mapping.building.spi.PojoMetadataContributor;
import org.hibernate.search.entity.pojo.mapping.building.spi.PojoMappingCollectorPropertyNode;
import org.hibernate.search.entity.pojo.model.additionalmetadata.building.spi.PojoAdditionalMetadataCollectorPropertyNode;
import org.hibernate.search.entity.pojo.mapping.definition.programmatic.PropertyDocumentIdMappingContext;
import org.hibernate.search.entity.pojo.mapping.definition.programmatic.PropertyMappingContext;


/**
 * @author Yoann Rodiere
 */
public class PropertyDocumentIdMappingContextImpl extends DelegatingPropertyMappingContext
		implements PropertyDocumentIdMappingContext,
		PojoMetadataContributor<PojoAdditionalMetadataCollectorPropertyNode, PojoMappingCollectorPropertyNode> {

	private BridgeBuilder<? extends IdentifierBridge<?>> bridgeBuilder;

	public PropertyDocumentIdMappingContextImpl(PropertyMappingContext parent) {
		super( parent );
	}

	@Override
	public void contributeModel(PojoAdditionalMetadataCollectorPropertyNode collector) {
		// Nothing to do
	}

	@Override
	public void contributeMapping(PojoMappingCollectorPropertyNode collector) {
		collector.identifierBridge( bridgeBuilder );
	}

	@Override
	public PropertyDocumentIdMappingContext identifierBridge(String bridgeName) {
		return identifierBridge( new ImmutableBeanReference( bridgeName ) );
	}

	@Override
	public PropertyDocumentIdMappingContext identifierBridge(Class<? extends IdentifierBridge<?>> bridgeClass) {
		return identifierBridge( new ImmutableBeanReference( bridgeClass ) );
	}

	@Override
	public PropertyDocumentIdMappingContext identifierBridge(String bridgeName, Class<? extends IdentifierBridge<?>> bridgeClass) {
		return identifierBridge( new ImmutableBeanReference( bridgeName, bridgeClass ) );
	}

	// The builder will return an object of some class T where T extends IdentifierBridge, so this is safe
	@SuppressWarnings( "unchecked" )
	private PropertyDocumentIdMappingContext identifierBridge(BeanReference bridgeReference) {
		return identifierBridge(
				(BridgeBuilder<? extends IdentifierBridge<?>>)
						new BeanResolverBridgeBuilder( IdentifierBridge.class, bridgeReference )
		);
	}

	@Override
	public PropertyDocumentIdMappingContext identifierBridge(BridgeBuilder<? extends IdentifierBridge<?>> builder) {
		this.bridgeBuilder = builder;
		return this;
	}
}
