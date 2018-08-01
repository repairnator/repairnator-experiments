/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.entity.orm.logging.impl;

import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;

/**
 * Message bundle for event contexts in the Hibernate ORM mapper.
 */
@MessageBundle(projectCode = "HSEARCH")
public interface HibernateOrmEventContextMessages {

	@Message(value = "Hibernate ORM mapping")
	String mapping();

}
