/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.backend.index.spi;

import org.hibernate.search.backend.document.DocumentElement;
import org.hibernate.search.backend.document.model.dsl.spi.IndexSchemaRootNodeBuilder;

/**
 * @author Yoann Rodiere
 */
public interface IndexManagerBuilder<D extends DocumentElement> {

	/**
	 * Close any allocated resource.
	 * <p>
	 * This method is called when an error occurs while starting up Hibernate Search.
	 * When this method is called, it is guaranteed to be the last call on the builder.
	 */
	void closeOnFailure();

	/**
	 * @return An {@link IndexSchemaRootNodeBuilder} allowing to contribute metadata about the index schema.
	 * <p>
	 * Never called after {@link #build()}.
	 */
	IndexSchemaRootNodeBuilder getSchemaRootNodeBuilder();

	/**
	 * Build the mapping based on the {@link #getSchemaRootNodeBuilder()} metadata contributed} so far.
	 * <p>
	 * May only be called once on a given object.
	 *
	 * @return The index manager.
	 */
	IndexManagerImplementor<D> build();

}
