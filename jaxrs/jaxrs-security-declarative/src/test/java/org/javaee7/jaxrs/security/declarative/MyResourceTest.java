package org.javaee7.jaxrs.security.declarative;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.javaee7.CliCommands;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

import com.meterware.httpunit.AuthorizationRequiredException;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.HttpException;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.PutMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class MyResourceTest {

    @ArquillianResource
    private URL base;

    private static final String WEBAPP_SRC = "src/main/webapp";

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        
        addUsersToContainerIdentityStore();
        
        return ShrinkWrap.create(WebArchive.class)
            .addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "web.xml")))
            .addClasses(MyApplication.class, MyResource.class);
    }

    @Test
    public void testGetWithCorrectCredentials() throws IOException, SAXException {
        WebConversation conv = new WebConversation();
        conv.setAuthentication("file", "u1", "p1");
        GetMethodWebRequest getRequest = new GetMethodWebRequest(base + "/webresources/myresource");
        WebResponse response = null;
        try {
            response = conv.getResponse(getRequest);
        } catch (AuthorizationRequiredException e) {
            fail(e.getMessage());
        }
        assertNotNull(response);
        assertTrue(response.getText().contains("get"));
    }

    @Test
    public void testGetSubResourceWithCorrectCredentials() throws IOException, SAXException {
        WebConversation conv = new WebConversation();
        conv.setAuthentication("file", "u1", "p1");
        GetMethodWebRequest getRequest = new GetMethodWebRequest(base + "/webresources/myresource/1");
        WebResponse response = null;
        try {
            response = conv.getResponse(getRequest);
        } catch (AuthorizationRequiredException e) {
            fail(e.getMessage());
        }
        assertNotNull(response);
        
        assertTrue(response.getText().contains("get1"));
    }

    @Test
    public void testGetWithIncorrectCredentials() throws IOException, SAXException {
        WebConversation conv = new WebConversation();
        conv.setAuthentication("file", "random", "random");
        GetMethodWebRequest getRequest = new GetMethodWebRequest(base + "/webresources/myresource");
        try {
            WebResponse response = conv.getResponse(getRequest);
        } catch (AuthorizationRequiredException e) {
            assertNotNull(e);
            return;
        }
        fail("GET can be called with incorrect credentials");
    }

    @Test
    public void testPost() throws IOException, SAXException {
        WebConversation conv = new WebConversation();
        conv.setAuthentication("file", "u1", "p1");
        PostMethodWebRequest postRequest = new PostMethodWebRequest(base + "/webresources/myresource");
        try {
            WebResponse response = conv.getResponse(postRequest);
        } catch (HttpException e) {
            assertNotNull(e);
            assertEquals(403, e.getResponseCode());
            return;
        }
        fail("POST is not authorized and can still be called");
    }

    @Test
    public void testPut() throws IOException, SAXException {
        WebConversation conv = new WebConversation();
        conv.setAuthentication("file", "u1", "p1");
        byte[] bytes = new byte[8];
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        PutMethodWebRequest putRequest = new PutMethodWebRequest(base + "/webresources/myresource", bais, "text/plain");
        try {
            WebResponse response = conv.getResponse(putRequest);
        } catch (HttpException e) {
            assertNotNull(e);
            assertEquals(403, e.getResponseCode());
            return;
        }
        fail("PUT is not authorized and can still be called");
    }
    
    private static void addUsersToContainerIdentityStore() {
        
        // TODO: abstract adding container managed users to utility class
        // TODO: consider PR for sending CLI commands to Arquillian
        
        String javaEEServer = System.getProperty("javaEEServer");
        
        if ("glassfish-remote".equals(javaEEServer)) {
            List<String> cmd = new ArrayList<>();
            
            cmd.add("create-file-user");
            cmd.add("--groups");
            cmd.add("g1");
            cmd.add("--passwordfile");
            cmd.add(Paths.get("").toAbsolutePath() + "/src/test/resources/password.txt");
            
            cmd.add("u1");
            
            CliCommands.payaraGlassFish(cmd);
        } 
        
        // TODO: support other servers than Payara and GlassFish
    }
}
