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
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * (c)reated 10.07.2017 by oboehm (ob@oasd.de)
 */
package de.jfachwert.rechnung;

import de.jfachwert.*;
import de.jfachwert.pruefung.*;

/**
 * Auf vielen Rechnungen findet man eine Rechnungsnummer wie "000002835042",
 * die durch diese Klasse repraesentiert werden kann.
 *
 * @author oboehm
 * @since 0.3 (10.07.2017)
 */
public class Rechnungsnummer extends AbstractFachwert<String> {

    /**
     * Erzeugt eine Rechnungsnummer.
     *
     * @param nummer z.B. "000002835042"
     */
    public Rechnungsnummer(String nummer) {
        this(nummer, LengthValidator.NOT_EMPTY_VALIDATOR);
    }

    /**
     * Dieser Konstruktor ist hauptsaechlich fuer abgeleitete Klassen gedacht,
     * damit diese das {@link PruefzifferVerfahren} ueberschreiben koennen.
     * Man kann es auch verwenden, um ein eigenes {@link PruefzifferVerfahren}
     * einsetzen zu koennen.
     *
     * @param nummer   z.B. "000002835042"
     * @param pruefung Pruefverfahren
     */
    public Rechnungsnummer(String nummer, PruefzifferVerfahren<String> pruefung) {
        super(pruefung.validate(nummer));
    }

}
