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
 * Copyright (c) 2017, Oracle and/or its affiliates. All rights reserved.
 */
package org.opensolaris.opengrok.configuration.messages;

import java.io.IOException;
import java.util.HashSet;
import org.opensolaris.opengrok.configuration.RuntimeEnvironment;

/**
 * Message for refreshing SearcherManagers in the webapp after partial reindex.
 * The list of projects is carried in tags.
 *
 * @author Vladimir Kotal
 */
public class RefreshMessage extends Message {
    @Override
    public byte[] applyMessage(RuntimeEnvironment env) throws IOException {
        env.maybeRefreshIndexSearchers(this.getTags());

        return null;
    }

    @Override
    public void validate() throws Exception {
        super.validate();
    }
}