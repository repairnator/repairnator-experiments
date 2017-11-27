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
 * Copyright (c) 2008, 2016, Oracle and/or its affiliates. All rights reserved.
 */
package org.opensolaris.opengrok.analysis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Definitions implements Serializable {

    private static final long serialVersionUID = 1191703801007779489L;

    // Per line sym -> tags mapping
    public static class LineTagMap implements Serializable {

        private static final long serialVersionUID = 1191703801007779481L;
        private final Map<String, Set<Tag>> sym_tags; //NOPMD

        protected LineTagMap() {
            this.sym_tags = new HashMap<>();
        }
    }
    // line -> tag_map
    private final Map<Integer, LineTagMap> line_maps;

    /**
     * Map from symbol to the line numbers on which the symbol is defined.
     */
    private final Map<String, Set<Integer>> symbols;
    /**
     * List of all the tags.
     */
    private final List<Tag> tags;

    public Definitions() {
        symbols = new HashMap<>();
        line_maps = new HashMap<>();
        tags = new ArrayList<>();
    }

    /**
     * Get all symbols used in definitions.
     *
     * @return a set containing all the symbols
     */
    public Set<String> getSymbols() {
        return symbols.keySet();
    }

    /**
     * Check if there is a tag for a symbol.
     *
     * @param symbol the symbol to check
     * @return {@code true} if there is a tag for {@code symbol}
     */
    public boolean hasSymbol(String symbol) {
        return symbols.containsKey(symbol);
    }

    /**
     * Check whether the specified symbol is defined on the given line.
     *
     * @param symbol the symbol to look for
     * @param lineNumber the line to check
     * @param strs type of definition(to be passed back to caller)
     * @return {@code true} if {@code symbol} is defined on the specified line
     */
    public boolean hasDefinitionAt(String symbol, int lineNumber, String[] strs) {
        Set<Integer> lines = symbols.get(symbol);
        if (strs.length > 0) {
            strs[0] = "none";
        }

        // Get tag info
        if (lines != null && lines.contains(lineNumber)) {
            LineTagMap line_map = line_maps.get(lineNumber);
            if (line_map != null) {
                for (Tag tag : line_map.sym_tags.get(symbol)) {
                    if (strs.length > 0) { //NOPMD
                        strs[0] = tag.type;
                    }
                    // Assume the first one
                    break;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Return the number of occurrences of definitions with the specified
     * symbol.
     *
     * @param symbol the symbol to count the occurrences of
     * @return the number of times the specified symbol is defined
     */
    public int occurrences(String symbol) {
        Set<Integer> lines = symbols.get(symbol);
        return lines == null ? 0 : lines.size();
    }

    /**
     * Return the number of distinct symbols.
     *
     * @return number of distinct symbols
     */
    public int numberOfSymbols() {
        return symbols.size();
    }

    /**
     * Get a list of all tags.
     *
     * @return all tags
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Get a list of all tags on given line.
     *
     * @param line line number
     * @return list of tags
     */
    public List<Tag> getTags(int line) {
        LineTagMap line_map = line_maps.get(line);
        List<Tag> result = null;

        if (line_map != null) {
            result = new ArrayList<>();
            for (Set<Tag> ltags : line_map.sym_tags.values()) {
                for (Tag tag : ltags) {
                    result.add(tag);
                }
            }
        }

        return result;
    }

    /**
     * Class that represents a single tag.
     */
    public static class Tag implements Serializable {

        private static final long serialVersionUID = 1217869075425651465L;

        /**
         * Line number of the tag.
         */
        public final int line;
        /**
         * The symbol used in the definition.
         */
        public final String symbol;
        /**
         * The type of the tag.
         */
        public final String type;
        /**
         * The full line on which the definition occurs.
         */
        public final String text;
        /**
         * Namespace/class of tag definition
         */
        public final String namespace;
        /**
         * Scope of tag definition
         */
        public final String signature;

        protected Tag(int line, String symbol, String type, String text, String namespace, String signature) {
            this.line = line;
            this.symbol = symbol;
            this.type = type;
            this.text = text;
            this.namespace = namespace;
            this.signature = signature;
        }
    }

    public void addTag(int line, String symbol, String type, String text) {
        addTag(line, symbol, type, text, null, null);
    }

    public void addTag(int line, String symbol, String type, String text, String namespace, String signature) {
        Tag new_tag = new Tag(line, symbol, type, text, namespace, signature);
        tags.add(new_tag);
        Set<Integer> lines = symbols.get(symbol);
        if (lines == null) {
            lines = new HashSet<>();
            symbols.put(symbol, lines);
        }
        Integer aLine = line;
        lines.add(aLine);

        // Get per line map
        LineTagMap line_map = line_maps.get(aLine);
        if (line_map == null) {
            line_map = new LineTagMap();
            line_maps.put(aLine, line_map);
        }

        // Insert sym->tag map for this line
        Set<Tag> ltags = line_map.sym_tags.get(symbol);
        if (ltags == null) {
            ltags = new HashSet<>();
            line_map.sym_tags.put(symbol, ltags);
        }
        ltags.add(new_tag);
    }

    /**
     * Create a binary representation of this object.
     *
     * @return a byte array representing this object
     * @throws IOException if an error happens when writing to the array
     */
    public byte[] serialize() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        new ObjectOutputStream(bytes).writeObject(this);
        return bytes.toByteArray();
    }

    /**
     * De-serialize a binary representation of a {@code Definitions} object.
     *
     * @param bytes a byte array containing the {@code Definitions} object
     * @return a {@code Definitions} object
     * @throws IOException if an I/O error happens when reading the array
     * @throws ClassNotFoundException if the class definition for an object
     * stored in the byte array cannot be found
     * @throws ClassCastException if the array contains an object of another
     * type than {@code Definitions}
     */
    public static Definitions deserialize(byte[] bytes)
            throws IOException, ClassNotFoundException {
        ObjectInputStream in
                = new ObjectInputStream(new ByteArrayInputStream(bytes));
        return (Definitions) in.readObject();
    }
}
