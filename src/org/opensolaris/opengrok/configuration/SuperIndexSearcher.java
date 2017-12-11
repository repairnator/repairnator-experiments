/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License (the "License").
 * You may not use this file except in compliance with the License.
 *
 * See LICENSE.txt included in this distribution for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at LICENSE.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 */

 /*
  * Copyright (c) 2016, Oracle and/or its affiliates. All rights reserved.
  */
package org.opensolaris.opengrok.configuration;

import java.util.concurrent.ExecutorService;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.SearcherManager;

/**
 * Wrapper class over IndexSearcher which keeps SearcherManager around so
 * that we can simply return the indexSearcher to it.
 *
 * @author vkotal
 */
public class SuperIndexSearcher extends IndexSearcher {
    SearcherManager searcherManager;

    public SuperIndexSearcher(IndexReader r) {
        super(r);
    }

    SuperIndexSearcher(IndexReader r, ExecutorService searchExecutor) {
        super(r, searchExecutor);
    }

    public void setSearcherManager(SearcherManager s) {
        searcherManager = s;
    }

    public SearcherManager getSearcherManager() {
        return (searcherManager);
    }
}
