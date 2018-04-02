package de.jfachwert;/*
 * Copyright (c) 2018 by Oliver Boehm
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
 * (c)reated 17.01.2018 by oboehm (ob@oasd.de)
 */

import org.junit.Test;

import javax.validation.ValidationException;

/**
 * Unit-Tests fuer {@link Text}-Klasse.
 *
 * @author oboehm
 */
public final class TextTest extends AbstractFachwertTest {

    @Override
    protected Text createFachwert() {
        return new Text("Hallo Welt!");
    }

    /**
     * Es sollte nicht moeglich sein, einen Null-Text anzulegen.
     */
    @Test(expected = ValidationException.class)
    public void testCtorNull() {
        new Text(null);
    }

}
