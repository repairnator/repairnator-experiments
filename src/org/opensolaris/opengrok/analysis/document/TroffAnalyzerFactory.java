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
 * Copyright (c) 2007, 2015, Oracle and/or its affiliates. All rights reserved.
 * Portions Copyright (c) 2017, Chris Fraire <cfraire@me.com>.
 */

package org.opensolaris.opengrok.analysis.document;

import java.io.InputStream;
import org.opensolaris.opengrok.analysis.FileAnalyzer;
import org.opensolaris.opengrok.analysis.FileAnalyzer.Genre;
import org.opensolaris.opengrok.analysis.FileAnalyzerFactory;

public class TroffAnalyzerFactory extends FileAnalyzerFactory {

    private static final String name = "Troff";

    public static final Matcher MATCHER = (byte[] contents, InputStream in) ->
        getTrueMatcher().isMagic(contents, in);

    public static final TroffAnalyzerFactory DEFAULT_INSTANCE =
        new TroffAnalyzerFactory();

    protected TroffAnalyzerFactory() {
        super(null, null, null, null, MATCHER, "text/plain", Genre.PLAIN, name);
    }

    @Override
    protected FileAnalyzer newAnalyzer() {
        return new TroffAnalyzer(this);
    }

    // Because DEFAULT_INSTANCE during its initialization uses the MATCHER,
    // while at the same time the DocumentMatcher in its initialization takes
    // a FileAnalyzerFactory, and because we want the instances to be the same
    // instance, then defer initialization of the DocumentMatcher using the
    // "16.6 Lazy initialization holder class idiom," written by Brian Goetz
    // and Tim Peierls with assistance from members of JCP JSR-166 Expert Group
    // and released to the public domain, as explained at
    // http://creativecommons.org/licenses/publicdomain .
    private static class TrueMatcherHolder {
        public static final DocumentMatcher MATCHER = new DocumentMatcher(
            DEFAULT_INSTANCE, new String[] {"'\\\"", ".so", ".\\\"", ".TH"});
    }

    private static DocumentMatcher getTrueMatcher() {
        return TrueMatcherHolder.MATCHER;
    }
}
