package org.javaee7.json.streaming.parser;

import java.io.File;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonReader;
import javax.json.stream.JsonParser;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.json.JSONException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class JsonParserFromStreamTest {

    @Deployment
    public static Archive<?> deploy() {
        File[] requiredLibraries = Maven.resolver().loadPomFromFile("pom.xml")
            .resolve("org.json:json")
            .withTransitivity().asFile();

        return ShrinkWrap.create(WebArchive.class)
            .addAsResource("1.json")
            .addAsResource("2.json")
            .addAsResource("3.json")
            .addAsResource("4.json")
            .addAsLibraries(requiredLibraries);
    }

    @Test
    public void testEmptyObject() throws JSONException {
        JsonParser parser = Json.createParser(Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream("/1.json"));

        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
    }

    @Test
    public void testSimpleObject() throws JSONException {
        JsonParser parser = Json.createParser(Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream("/2.json"));

        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
    }

    @Test
    public void testArray() throws JSONException {
        JsonParser parser = Json.createParser(Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream("/3.json"));

        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
    }

    @Test
    public void testNestedStructure() throws JSONException {
        JsonParser parser = Json.createParser(Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream("/4.json"));

        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.next());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.next());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.next());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
    }

}
