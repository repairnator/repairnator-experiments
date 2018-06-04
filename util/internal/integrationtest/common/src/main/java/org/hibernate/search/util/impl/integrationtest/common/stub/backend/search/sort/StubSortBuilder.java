/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.util.impl.integrationtest.common.stub.backend.search.sort;

import org.hibernate.search.search.dsl.sort.SortOrder;
import org.hibernate.search.search.sort.spi.DistanceSortBuilder;
import org.hibernate.search.search.sort.spi.FieldSortBuilder;
import org.hibernate.search.search.sort.spi.ScoreSortBuilder;
import org.hibernate.search.util.impl.integrationtest.common.stub.backend.search.StubQueryElementCollector;

public class StubSortBuilder implements ScoreSortBuilder<StubQueryElementCollector>,
		FieldSortBuilder<StubQueryElementCollector>, DistanceSortBuilder<StubQueryElementCollector> {

	@Override
	public void missingFirst() {
		// No-op
	}

	@Override
	public void missingLast() {
		// No-op
	}

	@Override
	public void missingAs(Object value) {
		// No-op
	}

	@Override
	public void order(SortOrder order) {
		// No-op
	}

	@Override
	public void contribute(StubQueryElementCollector collector) {
		collector.simulateCollectCall();
	}
}
