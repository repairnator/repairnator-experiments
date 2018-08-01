/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.elasticsearch.work.impl.builder;

import org.hibernate.search.elasticsearch.work.impl.ElasticsearchWork;
import org.hibernate.search.elasticsearch.work.impl.SearchResult;

/**
 * @author Yoann Rodiere
 */
public interface ScrollWorkBuilder extends ElasticsearchWorkBuilder<ElasticsearchWork<SearchResult>> {

}
