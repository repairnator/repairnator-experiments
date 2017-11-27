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
 * Copyright (c) 2006, 2017, Oracle and/or its affiliates. All rights reserved.
 */
package org.opensolaris.opengrok.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.CharArraySet;
import org.opensolaris.opengrok.analysis.plain.PlainFullTokenizer;

public final class HistoryAnalyzer extends Analyzer {

    private final CharArraySet stopWords;
    /**
     * An array containing some common English words that are not usually useful
     * for searching.
     */
    private static final String[] ENGLISH_STOP_WORDS = {
        "a", "an", "and", "are", "as", "at", "be", "but", "by",
        "for", "if", "in", "into", "is", "it",
        "no", "not", "of", "on", "or", "s", "such",
        "t", "that", "the", "their", "then", "there", "these",
        "they", "this", "to", "was", "will", "with",
        "/", "\\", ":", ".", "0.0", "1.0"
    };

    /**
     * Builds an analyzer which removes words in ENGLISH_STOP_WORDS.
     */
    public HistoryAnalyzer() {
        super(Analyzer.PER_FIELD_REUSE_STRATEGY);
        stopWords = StopFilter.makeStopSet(ENGLISH_STOP_WORDS);
    }
   
    /**
     * Filters LowerCaseTokenizer with StopFilter.
     * @param fieldName name of field for which to create components     
     * @return components for this analyzer
     */
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {        
        final PlainFullTokenizer plainfull = new PlainFullTokenizer(FileAnalyzer.dummyReader);
        //we are counting position increments, this might affect the queries later and need to be in sync, especially for highlighting of results
        return new TokenStreamComponents(plainfull, new StopFilter(plainfull, stopWords));
    }
}
