/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.keycloak.saml.common.util;

import org.keycloak.saml.common.ErrorCodes;
import org.keycloak.saml.common.PicketLinkLogger;
import org.keycloak.saml.common.PicketLinkLoggerFactory;
import org.keycloak.saml.common.constants.GeneralConstants;
import org.keycloak.saml.common.constants.JBossSAMLConstants;
import org.keycloak.saml.common.constants.JBossSAMLURIConstants;
import org.keycloak.saml.common.exceptions.ConfigurationException;
import org.keycloak.saml.common.exceptions.ParsingException;
import org.keycloak.saml.common.exceptions.ProcessingException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Utility for the stax based parser
 *
 * @author Anil.Saldhana@redhat.com
 * @since Feb 8, 2010
 */
public class StaxParserUtil {

    private static final PicketLinkLogger logger = PicketLinkLoggerFactory.getLogger();

    public static void validate(InputStream doc, InputStream sch) throws ParsingException {
        try {
            XMLEventReader xmlEventReader = StaxParserUtil.getXMLEventReader(doc);
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(sch));
            Validator validator = schema.newValidator();
            validator.validate(new StAXSource(xmlEventReader));
        } catch (Exception e) {
            throw logger.parserException(e);
        }

    }

    /**
     * Bypass an entire XML element block from startElement to endElement.
     * It is expected that the {@code xmlEventReader} is positioned at (has not yet read)
     * the start element of the block it should bypass.
     *
     * @param xmlEventReader
     * @param tag Tag of the XML element that we need to bypass
     *
     * @throws org.keycloak.saml.common.exceptions.ParsingException
     */
    public static void bypassElementBlock(XMLEventReader xmlEventReader, String tag) throws ParsingException {
        XMLEvent xmlEvent = bypassElementBlock(xmlEventReader);

        if (! (xmlEvent instanceof EndElement) || ! Objects.equals(((EndElement) xmlEvent).getName().getLocalPart(), tag)) {
            throw logger.parserExpectedEndTag(tag);
        }
    }

    /**
     * Bypass an entire XML element block from startElement to endElement.
     * It is expected that the {@code xmlEventReader} is positioned at (has not yet read)
     * the start element of the block it should bypass.
     *
     * @param xmlEventReader
     * @param tag Tag of the XML element that we need to bypass
     *
     * @throws org.keycloak.saml.common.exceptions.ParsingException
     */
    public static void bypassElementBlock(XMLEventReader xmlEventReader, JBossSAMLConstants tag) throws ParsingException {
        bypassElementBlock(xmlEventReader, tag == null ? null : tag.get());
    }

    /**
     * Bypass an entire XML element block.
     * It is expected that the {@code xmlEventReader} is positioned at (has not yet read)
     * the start element of the block it should bypass.
     *
     * @param xmlEventReader
     * @returns Last XML event which is {@link EndElement} corresponding to the first startElement when no error occurs ({@code null} if not available)
     *
     * @throws org.keycloak.saml.common.exceptions.ParsingException
     */
    public static XMLEvent bypassElementBlock(XMLEventReader xmlEventReader) throws ParsingException {
        XMLEvent xmlEvent;
        int levelOfNesting = 0;
        if (! xmlEventReader.hasNext()) {
            return null;
        }

        try {
            do {
                xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent instanceof StartElement) {
                    levelOfNesting++;
                } else if (xmlEvent instanceof EndElement) {
                    levelOfNesting--;
                }
            } while (levelOfNesting > 0 && xmlEventReader.hasNext());
        } catch (XMLStreamException e) {
            throw logger.parserException(e);
        }

        return xmlEvent;
    }



    /**
     * Advances reader if character whitespace encountered
     *
     * @param xmlEventReader
     * @param xmlEvent
     * @return
     */
    public static boolean wasWhitespacePeeked(XMLEventReader xmlEventReader, XMLEvent xmlEvent) {
        if (xmlEvent.isCharacters()) {
            Characters chars = xmlEvent.asCharacters();
            String data = chars.getData();
            if (data == null || data.trim().equals("")) {
                try {
                    xmlEventReader.nextEvent();
                    return true;
                } catch (XMLStreamException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }

    /**
     * Given an {@code Attribute}, get its trimmed value
     *
     * @param attribute
     *
     * @return
     */
    public static String getAttributeValue(Attribute attribute) {
        String str = trim(attribute.getValue());
        return str;
    }

    /**
     * Get the Attribute value
     *
     * @param startElement
     * @param tag localpart of the qname of the attribute
     *
     * @return
     */
    public static String getAttributeValue(StartElement startElement, String tag) {
        String result = null;
        Attribute attr = startElement.getAttributeByName(new QName(tag));
        if (attr != null)
            result = getAttributeValue(attr);
        return result;
    }

    /**
     * Get the Attribute value
     *
     * @param startElement
     * @param tag localpart of the qname of the attribute
     *
     * @return false if attribute not set
     */
    public static boolean getBooleanAttributeValue(StartElement startElement, String tag) {
        return getBooleanAttributeValue(startElement, tag, false);
    }

    /**
     * Get the Attribute value
     *
     * @param startElement
     * @param tag localpart of the qname of the attribute
     *
     * @return false if attribute not set
     */
    public static boolean getBooleanAttributeValue(StartElement startElement, String tag, boolean defaultValue) {
        String result = null;
        Attribute attr = startElement.getAttributeByName(new QName(tag));
        if (attr != null)
            result = getAttributeValue(attr);
        if (result == null) return defaultValue;
        return Boolean.valueOf(result);
    }

    /**
     * Given that the {@code XMLEventReader} is in {@code XMLStreamConstants.START_ELEMENT} mode, we parse into a DOM
     * Element
     *
     * @param xmlEventReader
     *
     * @return
     *
     * @throws ParsingException
     */
    public static Element getDOMElement(XMLEventReader xmlEventReader) throws ParsingException {
        Transformer transformer = null;

        final String JDK_TRANSFORMER_PROPERTY = "picketlink.jdk.transformer";

        boolean useJDKTransformer = Boolean.parseBoolean(SecurityActions.getSystemProperty(JDK_TRANSFORMER_PROPERTY, "false"));

        try {
            if (useJDKTransformer) {
                transformer = TransformerUtil.getTransformer();
            } else {
                transformer = TransformerUtil.getStaxSourceToDomResultTransformer();
            }

            Document resultDocument = DocumentUtil.createDocument();
            DOMResult domResult = new DOMResult(resultDocument);

            Source source = new StAXSource(xmlEventReader);

            TransformerUtil.transform(transformer, source, domResult);

            Document doc = (Document) domResult.getNode();
            return doc.getDocumentElement();
        } catch (ConfigurationException e) {
            throw logger.parserException(e);
        } catch (XMLStreamException e) {
            throw logger.parserException(e);
        }
    }

    /**
     * Get the element text.
     *
     * @param xmlEventReader
     *
     * @return A <b>trimmed</b> string value
     *
     * @throws ParsingException
     */
    public static String getElementText(XMLEventReader xmlEventReader) throws ParsingException {
        String str = null;
        try {
            str = xmlEventReader.getElementText().trim();
        } catch (XMLStreamException e) {
            throw logger.parserException(e);
        }
        return str;
    }

    /**
     * Get the XML event reader
     *
     * @param is
     *
     * @return
     */
    public static XMLEventReader getXMLEventReader(InputStream is) {
        XMLInputFactory xmlInputFactory;
        XMLEventReader xmlEventReader = null;
        try {
            xmlInputFactory = XML_INPUT_FACTORY.get();
            xmlEventReader = xmlInputFactory.createXMLEventReader(is);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return xmlEventReader;
    }

    private static AtomicBoolean XML_EVENT_READER_ON_SOURCE_SUPPORTED = new AtomicBoolean(true);

    /**
     * Get the XML event reader
     *
     * @param source
     *
     * @return
     */
    public static XMLEventReader getXMLEventReader(Source source) {
        XMLInputFactory xmlInputFactory = XML_INPUT_FACTORY.get();
        try {
            if (XML_EVENT_READER_ON_SOURCE_SUPPORTED.get()) {
                try {
                    // The following method is optional per specification
                    return xmlInputFactory.createXMLEventReader(source);
                } catch (UnsupportedOperationException ex) {
                    XML_EVENT_READER_ON_SOURCE_SUPPORTED.set(false);
                    return getXMLEventReader(source);
                }
            } else {
                return getXMLEventReader(DocumentUtil.getSourceAsStream(source));
            }
        } catch (ConfigurationException | ProcessingException | XMLStreamException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Given a {@code Location}, return a formatted string [lineNum,colNum]
     *
     * @param location
     *
     * @return
     */
    public static String getLineColumnNumber(Location location) {
        StringBuilder builder = new StringBuilder("[");
        builder.append(location.getLineNumber()).append(",").append(location.getColumnNumber()).append("]");
        return builder.toString();
    }

    /**
     * Get the next xml event
     *
     * @param xmlEventReader
     *
     * @return
     *
     * @throws ParsingException
     */
    public static XMLEvent getNextEvent(XMLEventReader xmlEventReader) throws ParsingException {
        try {
            return xmlEventReader.nextEvent();
        } catch (XMLStreamException e) {
            throw logger.parserException(e);
        }
    }

    /**
     * Get the next {@code StartElement }
     *
     * @param xmlEventReader
     *
     * @return
     *
     * @throws ParsingException
     */
    public static StartElement getNextStartElement(XMLEventReader xmlEventReader) throws ParsingException {
        try {
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();

                if (xmlEvent == null || xmlEvent.isStartElement())
                    return (StartElement) xmlEvent;
            }
        } catch (XMLStreamException e) {
            throw logger.parserException(e);
        }
        return null;
    }

    /**
     * Get the next {@code EndElement}
     *
     * @param xmlEventReader
     *
     * @return
     *
     * @throws ParsingException
     */
    public static EndElement getNextEndElement(XMLEventReader xmlEventReader) throws ParsingException {
        try {
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();

                if (xmlEvent == null || xmlEvent.isEndElement())
                    return (EndElement) xmlEvent;
            }
        } catch (XMLStreamException e) {
            throw logger.parserException(e);
        }
        return null;
    }

    /**
     * Return the name of the start element
     *
     * @param startElement
     *
     * @return
     */
    public static String getStartElementName(StartElement startElement) {
        return trim(startElement.getName().getLocalPart());
    }

    /**
     * Return the name of the end element
     *
     * @param endElement
     *
     * @return
     */
    public static String getEndElementName(EndElement endElement) {
        return trim(endElement.getName().getLocalPart());
    }

    /**
     * Given a start element, obtain the xsi:type defined
     *
     * @param startElement
     *
     * @return
     *
     * @throws RuntimeException if xsi:type is missing
     */
    public static String getXSITypeValue(StartElement startElement) {
        Attribute xsiType = startElement.getAttributeByName(new QName(JBossSAMLURIConstants.XSI_NSURI.get(),
                JBossSAMLConstants.TYPE.get()));
        if (xsiType == null)
            throw logger.parserExpectedXSI(ErrorCodes.EXPECTED_XSI);
        return StaxParserUtil.getAttributeValue(xsiType);
    }

    /**
     * Return whether the next event is going to be text
     *
     * @param xmlEventReader
     *
     * @return
     *
     * @throws ParsingException
     */
    public static boolean hasTextAhead(XMLEventReader xmlEventReader) throws ParsingException {
        XMLEvent event = peek(xmlEventReader);
        return event.getEventType() == XMLEvent.CHARACTERS;
    }

    /**
     * Match that the start element with the expected tag
     *
     * @param startElement
     * @param tag
     *
     * @return boolean if the tags match
     */
    public static boolean matches(StartElement startElement, String tag) {
        String elementTag = getStartElementName(startElement);
        return tag.equals(elementTag);
    }

    /**
     * Match that the end element with the expected tag
     *
     * @param endElement
     * @param tag
     *
     * @return boolean if the tags match
     */
    public static boolean matches(EndElement endElement, String tag) {
        String elementTag = getEndElementName(endElement);
        return tag.equals(elementTag);
    }

    /**
     * Peek at the next event
     *
     * @param xmlEventReader
     *
     * @return
     *
     * @throws ParsingException
     */
    public static XMLEvent peek(XMLEventReader xmlEventReader) throws ParsingException {
        try {
            return xmlEventReader.peek();
        } catch (XMLStreamException e) {
            throw logger.parserException(e);
        }
    }

    /**
     * Peek the next {@code StartElement }
     *
     * @param xmlEventReader
     *
     * @return
     *
     * @throws ParsingException
     */
    public static StartElement peekNextStartElement(XMLEventReader xmlEventReader) throws ParsingException {
        try {
            while (true) {
                XMLEvent xmlEvent = xmlEventReader.peek();

                if (xmlEvent == null || xmlEvent.isStartElement())
                    return (StartElement) xmlEvent;
                else
                    xmlEvent = xmlEventReader.nextEvent();
            }
        } catch (XMLStreamException e) {
            throw logger.parserException(e);
        }
    }

    /**
     * Peek the next {@code EndElement}
     *
     * @param xmlEventReader
     *
     * @return
     *
     * @throws ParsingException
     */
    public static EndElement peekNextEndElement(XMLEventReader xmlEventReader) throws ParsingException {
        try {
            while (true) {
                XMLEvent xmlEvent = xmlEventReader.peek();

                if (xmlEvent == null || xmlEvent.isEndElement())
                    return (EndElement) xmlEvent;
                else
                    xmlEvent = xmlEventReader.nextEvent();
            }
        } catch (XMLStreamException e) {
            throw logger.parserException(e);
        }
    }

    /**
     * Given a string, trim it
     *
     * @param str
     *
     * @return
     *
     * @throws {@code IllegalArgumentException} if the passed str is null
     */
    public static final String trim(String str) {
        if (str == null)
            throw logger.nullArgumentError("String to trim");
        return str.trim();
    }

    /**
     * Validate that the start element has the expected tag
     *
     * @param startElement
     * @param tag
     *
     * @throws RuntimeException mismatch
     */
    public static void validate(StartElement startElement, String tag) {
        String foundElementTag = getStartElementName(startElement);
        if (!tag.equals(foundElementTag))
            throw logger.parserExpectedTag(tag, foundElementTag);
    }

    /**
     * Validate that the end element has the expected tag
     *
     * @param endElement
     * @param tag
     *
     * @throws RuntimeException mismatch
     */
    public static void validate(EndElement endElement, String tag) {
        String elementTag = getEndElementName(endElement);
        if (!tag.equals(elementTag))
            throw new RuntimeException(logger.parserExpectedEndTag("</" + tag + ">.  Found </" + elementTag + ">"));
    }

    private static final ThreadLocal<XMLInputFactory> XML_INPUT_FACTORY = new ThreadLocal<XMLInputFactory>() {
        @Override
        protected XMLInputFactory initialValue() {
            return getXMLInputFactory();
        }
    };

    private static XMLInputFactory getXMLInputFactory() {
        boolean tccl_jaxp = SystemPropertiesUtil.getSystemProperty(GeneralConstants.TCCL_JAXP, "false")
                .equalsIgnoreCase("true");
        ClassLoader prevTCCL = SecurityActions.getTCCL();
        try {
            if (tccl_jaxp) {
                SecurityActions.setTCCL(StaxParserUtil.class.getClassLoader());
            }
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

            xmlInputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
            xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
            xmlInputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
            xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);

            return xmlInputFactory;
        } finally {
            if (tccl_jaxp) {
                SecurityActions.setTCCL(prevTCCL);
            }
        }
    }
}