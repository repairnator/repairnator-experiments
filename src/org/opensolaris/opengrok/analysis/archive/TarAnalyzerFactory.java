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
 */

package org.opensolaris.opengrok.analysis.archive;

import org.opensolaris.opengrok.analysis.FileAnalyzer;
import org.opensolaris.opengrok.analysis.FileAnalyzer.Genre;
import org.opensolaris.opengrok.analysis.FileAnalyzerFactory;

public class TarAnalyzerFactory extends FileAnalyzerFactory {
    
    private static final String name = "Tar";
    
    private static final String[] SUFFIXES = {
        "TAR"
    };

    public TarAnalyzerFactory() {
        super(null, null, SUFFIXES, null, null, null, Genre.XREFABLE, name);
    }

    @Override
    protected FileAnalyzer newAnalyzer() {
        return new TarAnalyzer(this);
    }
}
