/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.util.impl.integrationtest.common.stub.backend.search.sort;

import java.util.List;

import org.hibernate.search.search.SearchSort;

class StubSearchSort implements SearchSort {
	private final List<StubSortBuilder> builders;

	StubSearchSort(List<StubSortBuilder> builders) {
		this.builders = builders;
	}

	List<StubSortBuilder> getBuilders() {
		return builders;
	}
}
