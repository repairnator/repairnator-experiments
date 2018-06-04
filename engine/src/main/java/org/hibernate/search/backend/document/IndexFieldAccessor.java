/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.backend.document;


/**
 * An accessor to a specific field of an index document,
 * allowing to add new values to this field for a given document.
 *
 * @author Yoann Rodiere
 */
public interface IndexFieldAccessor<T> {

	/**
	 * Add a new value to the given state for the field targeted by this reference.
	 * <p>
	 * This method can be called multiple times for the same target, which will result
	 * in multiple values being added to the same field.
	 *
	 * @param target The parent to which the field value will be added.
	 * @param value The value to add to the field.
	 */
	void write(DocumentElement target, T value);

}
