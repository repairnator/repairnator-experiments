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

package org.opensolaris.opengrok.history;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opensolaris.opengrok.logger.LoggerFactory;
import org.opensolaris.opengrok.util.Executor;

/**
 * BitKeeperTagParser handles parsing the output of `bk tags` into a set of tag entries.
 *
 * @author James Service  {@literal <jas2701@googlemail.com>}
 */
public class BitKeeperTagParser implements Executor.StreamHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BitKeeperTagParser.class);

    /**
     * Parses dates.
     */
    private final SimpleDateFormat dateFormat;
    /**
     * Store tag entries created by processStream.
     */
    private final TreeSet<TagEntry> entries = new TreeSet<TagEntry>();

    /**
     * Constructor to construct the thing to be constructed.
     *
     * @param datePattern a simple date format string
     */
    public BitKeeperTagParser(String datePattern) {
        dateFormat = new SimpleDateFormat(datePattern);
    }

    /**
     * Returns the set of entries that has been created.
     *
     * @return entries a set of tag entries
     */
    public TreeSet<TagEntry> getEntries() {
        return entries;
    }

    /**
     * Process the output of a `bk tags` command.
     *
     * Each input line should be in the following format:
     * either
     *   D REVISION\tDATE
     * or
     *   T TAG
     *
     * @param input the executor input stream
     * @throws IOException if the stream reader throws an IOException
     */
    @Override
    public void processStream(InputStream input) throws IOException {
        String revision = null;
        Date date = null;
        String tag = null;

        final BufferedReader in = new BufferedReader(new InputStreamReader(input));
        for (String line = in.readLine(); line != null; line = in.readLine()) {
            if (line.startsWith("D ")) {
                final String fields[] = line.substring(2).split("\t");
                try {
                    revision = fields[0];
                    date = dateFormat.parse(fields[1]);
                } catch (final Exception e) {
                    LOGGER.log(Level.SEVERE, "Error: malformed BitKeeper tags output {0}", line);
                }
            } else if (line.startsWith("T ")) {
                if (date != null) {
                    tag = line.substring(2);
                    entries.add(new BitKeeperTagEntry(revision, date, tag));
                }
            }
        }
    }
}
