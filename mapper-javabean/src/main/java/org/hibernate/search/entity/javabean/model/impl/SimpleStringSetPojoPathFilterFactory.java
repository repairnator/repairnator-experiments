/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.entity.javabean.model.impl;

import java.util.Set;

import org.hibernate.search.entity.pojo.model.path.PojoModelPathPropertyNode;
import org.hibernate.search.entity.pojo.model.path.PojoModelPathValueNode;
import org.hibernate.search.entity.pojo.model.path.spi.PojoPathFilter;
import org.hibernate.search.entity.pojo.model.path.spi.PojoPathFilterFactory;
import org.hibernate.search.entity.pojo.model.path.spi.StringSetPojoPathFilter;
import org.hibernate.search.util.impl.common.CollectionHelper;

/**
 * A factory for filters expecting a simple string representation of dirty paths,
 * in the form "propertyA.propertyB.propertyC".
 * <p>
 * See {@link PojoModelPathPropertyNode#toPropertyString()}.
 */
public class SimpleStringSetPojoPathFilterFactory implements PojoPathFilterFactory<Set<String>> {
	@Override
	public PojoPathFilter<Set<String>> create(Set<PojoModelPathValueNode> paths) {
		// Use a LinkedHashSet for deterministic iteration
		Set<String> pathsAsStrings = CollectionHelper.newLinkedHashSet( paths.size() );
		for ( PojoModelPathValueNode path : paths ) {
			pathsAsStrings.add( path.getParent().toPropertyString() );
		}
		return new StringSetPojoPathFilter( pathsAsStrings );
	}
}
