/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.entity.javabean;

import org.hibernate.search.entity.pojo.mapping.PojoMapping;
import org.hibernate.search.entity.pojo.mapping.PojoSearchManager;

public interface JavaBeanMapping extends PojoMapping {

	PojoSearchManager createSearchManager();

	JavaBeanSearchManagerBuilder createSearchManagerWithOptions();

}
