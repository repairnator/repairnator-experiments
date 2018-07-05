package org.apache.servicecomb.demo.utils;

import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.JavaType;

public class JAXBUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(JAXBUtils.class);

  public static String convertToXml(Object obj) {
    return convertToXml(obj, StandardCharsets.UTF_8.toString());
  }

  public static String convertToXml(Object obj, String encoding) {
    String result = null;
    try {
      JAXBContext context = JAXBContext.newInstance(obj.getClass());
      Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

      StringWriter writer = new StringWriter();
      marshaller.marshal(obj, writer);
      result = writer.toString();
    } catch (Exception e) {
      LOGGER.error("Bean convert to xml failed, error message: {}", e.getMessage());
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  public static <T> T convertToJavaBean(InputStream xml, JavaType type) {
    return (T) convertToJavaBean(xml, type.getRawClass());
  }

  @SuppressWarnings("unchecked")
  public static <T> T convertToJavaBean(InputStream xml, Class<T> c) {
    T t = null;
    try {
      JAXBContext context = JAXBContext.newInstance(c);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      t = (T) unmarshaller.unmarshal(XXEPrevention(xml));
    } catch (Exception e) {
      LOGGER.error("Xml convert to Bean failed, error message: {}", e.getMessage());
    }
    return t;
  }

  private static Source XXEPrevention(InputStream xml) {
    Source xmlSource = null;
    SAXParserFactory spf = SAXParserFactory.newInstance();
    try {
      spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
      spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
      spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      xmlSource = new SAXSource(spf.newSAXParser().getXMLReader(), new InputSource(xml));
    } catch (Exception e) {
      LOGGER.error("Xml External Entity (XXE) Processing report error, error message: {}", e.getMessage());
    }
    return xmlSource;
  }
}
