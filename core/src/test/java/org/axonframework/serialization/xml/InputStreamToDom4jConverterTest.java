/*
 * Copyright (c) 2010-2012. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.serialization.xml;

import org.dom4j.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * @author Allard Buijze
 */
public class InputStreamToDom4jConverterTest {

    private InputStreamToDom4jConverter testSubject;

    @Before
    public void setUp() {
        testSubject = new InputStreamToDom4jConverter();
    }

    @Test
    public void testConvert() {
        byte[] bytes = "<parent><child/></parent>".getBytes();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        Document actual = testSubject.convert(inputStream);

        assertEquals("parent", actual.getRootElement().getName());
    }
}
