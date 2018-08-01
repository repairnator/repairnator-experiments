/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.entity.pojo.bridge.builtin.impl;

import org.hibernate.search.entity.pojo.bridge.IdentifierBridge;

public final class DefaultIntegerIdentifierBridge implements IdentifierBridge<Integer> {

	@Override
	public String toDocumentIdentifier(Integer propertyValue) {
		return propertyValue.toString();
	}

	@Override
	public Integer fromDocumentIdentifier(String documentIdentifier) {
		return Integer.parseInt( documentIdentifier );
	}

}