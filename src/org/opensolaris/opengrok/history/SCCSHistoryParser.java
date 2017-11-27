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
 * Copyright (c) 2007, 2017, Oracle and/or its affiliates. All rights reserved.
 */
package org.opensolaris.opengrok.history;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opensolaris.opengrok.logger.LoggerFactory;
import org.opensolaris.opengrok.util.IOUtils;

/**
 * Reads and filters out junk from a SCCS history file.
 * See sccsfile(4) for details of the file format.
 * Wrote it since invoking 'sccs prs' for each file was
 * taking a lot of time. Time to index history has reduced 4 to 1!
 */
class SCCSHistoryParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(SCCSHistoryParser.class);

    boolean pass;
    boolean passRecord;
    boolean active;
    int field;
    boolean sep;
    StringBuilder record = new StringBuilder(128);
    DateFormat sccsDateFormat;
    Reader in;

    // Record fields
    private String revision;
    private Date rdate;
    private String author;
    private String comment;

    History parse(File file, Repository repos) throws HistoryException {
        sccsDateFormat = repos.getDateFormat();
        try {
            return parseFile(file);
        } catch (IOException e) {
            throw new HistoryException("Failed to get history for " +
                "\"" + file.getAbsolutePath() + "\":", e);
        } catch (ParseException e) {
            throw new HistoryException("Failed to parse history for " +
                "\"" + file.getAbsolutePath() + "\":", e);
        }
    }

    private History parseFile(File file) throws IOException, ParseException {
        File f = getSCCSFile(file);
        if (f == null) {
            return null;
        }

        in = new BufferedReader(new FileReader(getSCCSFile(file)));
        pass = sep = false;
        passRecord = true;
        active = true;
        field = 0;
        sccsDateFormat =  new SimpleDateFormat("yy/MM/dd", Locale.getDefault());

        ArrayList<HistoryEntry> entries = new ArrayList<HistoryEntry>();
        while (next()) {
            HistoryEntry entry = new HistoryEntry();
            entry.setRevision(getRevision());
            entry.setDate(getDate());
            entry.setAuthor(getAuthor());
            entry.setMessage(getComment());
            entry.setActive(isActive());
            entries.add(entry);
        }

        IOUtils.close(in);

        History history = new History();
        history.setHistoryEntries(entries);
        return history;
    }

    /**
     * Read a single line of delta record into the 'record' member.
     *
     * @throws java.io.IOException
     * @return boolean indicating whether there is another record.
     */
    private boolean next() throws java.io.IOException {
        sep = true;
        record.setLength(0);
        int c;
        while ((c = read()) > 01) {
            record.append((char)c);
        }
        // to flag that revision needs to be re populated if you really need it
        revision = null;
        return (record.length() > 2);
    }

    /**
     * Split record into fields.
     *
     * @throws java.io.IOException
     */
    private void initFields() throws ParseException {
        if (revision == null) {
            String[] f = record.toString().split(" ", 6);
            if (f.length > 5) {
                revision = f[1];
                try {
                    rdate = sccsDateFormat.parse(f[2] + " " + f[3]);
                } catch (ParseException e) {
                    rdate = null;
                    //
                    // Throw new exception up so that it can be paired with filename
                    // on which the problem occurred.
                    //
                    throw e;
                }
                author = f[4];
                comment = f[5];
            } else {
                rdate = null;
                author = null;
                comment = null;
            }
        }
    }

    /**
     * @return  get the revision string of current log record
     */
    private String getRevision() throws ParseException {
        initFields();
        return revision;
    }

    /**
     * @return  get the date associated with current log record
     */
    private Date getDate() throws ParseException {
        initFields();
        return rdate;
    }

    /**
     * @return  get the author of current log record
     */
    private String getAuthor() throws ParseException {
        initFields();
        return author;
    }
    /**
     * @return  get the comments of current log record
     */
    private String getComment() throws ParseException {
        initFields();
        return comment;
    }

    private boolean isActive() {
        return active;
    }

    private int read() throws java.io.IOException {
        int c, d, dt;
        while((c = in.read()) != -1) {
            switch (c) { //NOPMD
                case 01:
                    d = in.read();
                    switch (d) {
                        case 'c':
                        case 't':
                        case 'u':
                            d = in.read();
                            if(d != ' ') {
                                return (d);
                            }
                            pass = true;
                            break;
                        case 'd':
                            d = in.read();
                            if (d == ' ') {
                                dt = in.read();
                                if (dt == 'R') {
                                    active = false;
                                } else {
                                    active = true;
                                }
                                passRecord = true;
                                field = 1;
                            } else {
                                return (d);
                            }
                            break;
                        case -1:
                        case 'I':   //the file contents start
                        case 'D':
                        case 'E':
                        case 'T':
                            return(-1);
                        case 'e':
                            pass = false;
                            if (sep && passRecord) {
                                return 01;
                            }
                            passRecord = true;
                            break;
                        default:
                            pass = false;
                    }
                    break;
                case ' ':
                    if (passRecord) {
                        if (field > 0) {
                            field ++;
                            pass = true;
                        }
                        if(field > 5) {
                            field = 0;
                            pass = false;
                            return(c);
                        }
                    }
                default:
                    if (pass && passRecord) {
                        return(c);
                    }
            }
        }
        return(-1);
    }

    protected static File getSCCSFile(File file)
    {
        return getSCCSFile(file.getParent(), file.getName());
    }

    protected static File getSCCSFile(String parent, String name)
    {
        File f = new File(parent + "/SCCS/s." + name);
        if (!f.exists()) {
            return null;
        }
        return f;
    }
}
