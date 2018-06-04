/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.backend.elasticsearch.search.sort.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;


/**
 * A sort collector for Elasticsearch, using JSON to represent sorts.
 * <p>
 * Used by Elasticsearch-specific sort contributors.
 *
 * @see org.hibernate.search.search.sort.spi.SearchSortContributor
 * @see org.hibernate.search.backend.elasticsearch.search.sort.impl.AbstractSearchSortBuilder
 */
public interface ElasticsearchSearchSortCollector {

	void collectSort(JsonArray sorts);

	void collectSort(JsonElement sort);

}
