/*
 * Copyright (c) 2017 by Oliver Boehm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express orimplied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * (c)reated 16.03.2017 by oboehm (ob@jfachwert.de)
 */
package de.jfachwert.bank;

import de.jfachwert.AbstractFachwert;
import de.jfachwert.pruefung.NumberValidator;
import org.apache.commons.lang3.StringUtils;

/**
 * Die BLZ (Bankleitzahl) ist eine eindeutige Kennziffer, die in Deutschland
 * und Oesterreich eindeutig ein Kreditinstitut identifiziert. In Deutschland
 * ist die BLZ eine 8-stellige, in Oesterreich eine 5-stellige Zahl (mit
 * Ausnahme der Oesterreichischen Nationalbank mit 3 Stellen).
 * 
 *
 * @author oboehm
 * @since 16.03.2017
 */
public class BLZ extends AbstractFachwert<String> {

    /**
     * Hierueber wird eine neue BLZ angelegt.
     *
     * @param code eine 5- oder 8-stellige Zahl
     */
    public BLZ(String code) {
        super(validate(code));
    }

    /**
     * Hierueber wird eine neue BLZ angelegt.
     *
     * @param code eine 5- oder 8-stellige Zahl
     */
    public BLZ(int code) {
        this(Integer.toString(code));
    }

    /**
     * Eine BLZ darf maximal 8-stellig sein.
     *
     * @param blz die Bankleitzahl
     * @return die Bankleitzahl zur Weitervarabeitung
     */
    public static int validate(int blz) {
        validate(Integer.toString(blz));
        return blz;
    }

    /**
     * Eine BLZ darf maximal 8-stellig sein.
     *
     * @param blz die Bankleitzahl
     * @return die Bankleitzahl zur Weitervarabeitung
     */
    public static String validate(String blz) {
        String normalized = StringUtils.replaceAll(blz, "\\s", "");
        return new NumberValidator(100, 99_999_999).validate(normalized);
    }

    /**
     * Liefert die unformattierte BLZ.
     *
     * @return unformattierte BLZ, z.B. "64090100"
     */
    public String getUnformatted() {
        return this.getCode();
    }
    
    /**
     * Liefert die BLZ in 3er-Gruppen formattiert.
     *
     * @return formatierte LBZ, z.B. "640 901 00"
     */
    public String getFormatted() {
        String input = this.getUnformatted() + "   ";
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < this.getUnformatted().length(); i+= 3) {
            buf.append(input.substring(i, i+3));
            buf.append(' ');
        }
        return buf.toString().trim();
    }
    
}
