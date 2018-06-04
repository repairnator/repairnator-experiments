/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.entity.orm.jpa;

import java.util.Collection;
import javax.persistence.EntityManager;

public interface FullTextEntityManager extends EntityManager {

	FullTextSearchTarget<Object> search();

	<T> FullTextSearchTarget<T> search(Class<T> type);

	<T> FullTextSearchTarget<T> search(Collection<? extends Class<? extends T>> types);

}
