/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.entity.javabean.mapping.impl;

import org.hibernate.search.entity.javabean.JavaBeanMapping;
import org.hibernate.search.entity.javabean.log.impl.JavaBeanEventContextMessages;
import org.hibernate.search.entity.mapping.spi.MappingKey;

import org.jboss.logging.Messages;

public final class JavaBeanMappingKey implements MappingKey<JavaBeanMapping> {
	private static final JavaBeanEventContextMessages MESSAGES =
			Messages.getBundle( JavaBeanEventContextMessages.class );

	@Override
	public String render() {
		return MESSAGES.mapping();
	}
}
