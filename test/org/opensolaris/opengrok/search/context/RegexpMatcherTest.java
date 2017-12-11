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
 * Copyright (c) 2008, 2015, Oracle and/or its affiliates. All rights reserved.
 */

package org.opensolaris.opengrok.search.context;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the RegexpMatcher class.
 */
public class RegexpMatcherTest {
    
    /**
     * Test of match method.
     */
    @Test
    public void testMatch() {
        RegexpMatcher m = new RegexpMatcher("reg[e]+x", true);
        assertEquals(LineMatcher.NOT_MATCHED, m.match("regx"));
        assertEquals(LineMatcher.MATCHED, m.match("regeex"));
        
        m = new RegexpMatcher("Reg[e]+x", false);
        assertEquals(LineMatcher.NOT_MATCHED, m.match("regex"));
        assertEquals(LineMatcher.MATCHED, m.match("Regex"));
    }    
    
}
