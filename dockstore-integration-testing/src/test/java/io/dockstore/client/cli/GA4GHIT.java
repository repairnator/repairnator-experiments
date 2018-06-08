/*
 *    Copyright 2017 OICR
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package io.dockstore.client.cli;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dockstore.common.CommonTestUtilities;
import io.dockstore.webservice.DockstoreWebserviceApplication;
import io.dockstore.webservice.DockstoreWebserviceConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.DropwizardTestSupport;
import io.swagger.client.model.ToolDescriptor;
import io.swagger.client.model.ToolTests;
import io.swagger.model.Error;
import org.apache.http.HttpStatus;
import org.glassfish.jersey.client.ClientProperties;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.dockstore.common.CommonTestUtilities.WAIT_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * @author gluu
 * @since 03/01/18
 */
public abstract class GA4GHIT {
    protected static final DropwizardTestSupport<DockstoreWebserviceConfiguration> SUPPORT = new DropwizardTestSupport<>(
            DockstoreWebserviceApplication.class, CommonTestUtilities.CONFIG_PATH);
    static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    static {
        DockstoreWebserviceApplication.configureMapper(MAPPER);
    }
    protected static javax.ws.rs.client.Client client;
    final String basePath = String.format("http://localhost:%d/" + getApiVersion(), SUPPORT.getLocalPort());

    @BeforeClass
    public static void dropAndRecreateDB() throws Exception {
        CommonTestUtilities.dropAndCreateWithTestData(SUPPORT, true);
        SUPPORT.before();
        client = new JerseyClientBuilder(SUPPORT.getEnvironment()).build("test client").property(ClientProperties.READ_TIMEOUT, WAIT_TIME);
    }

    @AfterClass
    public static void afterClass() {
        SUPPORT.after();
    }

    protected abstract String getApiVersion();

    /**
     * This tests the /metadata endpoint
     *
     * @throws Exception
     */
    protected abstract void metadata() throws Exception;

    /**
     * This tests the /tools endpoint
     *
     * @throws Exception
     */
    protected abstract void tools() throws Exception;

    /**
     * This tests the /tools/{id} endpoint
     *
     * @throws Exception
     */
    protected abstract void toolsId() throws Exception;

    /**
     * This tests the /tools/{id}/versions endpoint
     *
     * @throws Exception
     */
    protected abstract void toolsIdVersions() throws Exception;

    /**
     * This tests the /tool-classes or /toolClasses endpoint
     *
     * @throws Exception
     */
    protected abstract void toolClasses() throws Exception;

    /**
     * This tests the /tools/{id}/versions/{version_id} endpoint
     *
     * @throws Exception
     */
    protected abstract void toolsIdVersionsVersionId() throws Exception;

    /**
     * This tests the /tools/{id}/versions/{version-id}/{type}/descriptor endpoint
     *
     * @throws Exception
     */
    @Test
    public void toolsIdVersionsVersionIdTypeDescriptor() throws Exception {
        Response response = checkedResponse(basePath + "tools/quay.io%2Ftest_org%2Ftest6/versions/fakeName/CWL/descriptor");
        ToolDescriptor responseObject = response.readEntity(ToolDescriptor.class);
        assertThat(response.getStatus()).isEqualTo(200);
        assertDescriptor(MAPPER.writeValueAsString(responseObject));
    }

    /**
     * This tests the /tools/{id}/versions/{version_id}/{type}/descriptor/{relative_path} endpoint
     *
     * @throws Exception
     */

    @Test
    public void toolsIdVersionsVersionIdTypeDescriptorRelativePath() throws Exception {
        toolsIdVersionsVersionIdTypeDescriptorRelativePathNormal();
        toolsIdVersionsVersionIdTypeDescriptorRelativePathMissingSlash();
        toolsIdVersionsVersionIdTypeDescriptorRelativePathExtraDot();
    }

    private void toolsIdVersionsVersionIdTypeDescriptorRelativePathNormal() throws Exception {
        Response response = checkedResponse(
                basePath + "tools/quay.io%2Ftest_org%2Ftest6/versions/fakeName/CWL/descriptor/%2FDockstore.cwl");
        ToolDescriptor responseObject = response.readEntity(ToolDescriptor.class);
        assertThat(response.getStatus()).isEqualTo(200);
        assertDescriptor(MAPPER.writeValueAsString(responseObject));
    }

    /**
     * Tests PLAIN GET /tools/{id}/versions/{version_id}/{type}/descriptor/{relative_path} with:
     * Tool with nested cwl test parameter file
     * Tool with non-existent cwl test parameter file
     * Tool with nested wdl test parameter file
     * Tool with non-existent wdl test parameter file
     * @throws Exception
     */
    @Test
    public void relativePathEndpointToolTestParameterFilePLAIN() {
        Response response = checkedResponse(
            basePath + "tools/quay.io%2Ftest_org%2Ftest6/versions/fakeName/PLAIN_CWL/descriptor/%2Fnested%2Ftest.cwl.json");
        String responseObject = response.readEntity(String.class);
        assertEquals(200, response.getStatus());
        assertEquals("nestedPotato", responseObject);
        Response response2 = client.target(basePath + "tools/quay.io%2Ftest_org%2Ftest6/versions/fakeName/PLAIN_CWL/descriptor/%2Ftest.potato.json").request().get();
        assertEquals(404, response2.getStatus());
        Response response3 = checkedResponse(
            basePath + "tools/quay.io%2Ftest_org%2Ftest6/versions/fakeName/PLAIN_WDL/descriptor/%2Fnested%2Ftest.wdl.json");
        String responseObject3 = response3.readEntity(String.class);
        assertEquals(200, response3.getStatus());
        assertEquals("nestedPotato", responseObject3);
        Response response4 = client.target(basePath + "tools/quay.io%2Ftest_org%2Ftest6/versions/fakeName/PLAIN_WDL/descriptor/%2Ftest.potato.json").request().get();
        assertEquals(404, response4.getStatus());
    }

    /**
     * Tests JSON GET /tools/{id}/versions/{version_id}/{type}/descriptor/{relative_path} with:
     * Tool with nested cwl test parameter file
     * Tool with non-existent cwl test parameter file
     * Tool with nested wdl test parameter file
     * Tool with non-existent wdl test parameter file
     */
    @Test
    public void relativePathEndpointToolTestParameterFileJSON() {
        Response response = checkedResponse(
            basePath + "tools/quay.io%2Ftest_org%2Ftest6/versions/fakeName/CWL/descriptor/%2Fnested%2Ftest.cwl.json");
        ToolTests responseObject = response.readEntity(ToolTests.class);
        assertEquals(200, response.getStatus());
        assertEquals("nestedPotato", responseObject.getTest());
        Response response2 = checkedResponse(
            basePath + "tools/quay.io%2Ftest_org%2Ftest6/versions/fakeName/WDL/descriptor/%2Fnested%2Ftest.wdl.json");
        ToolTests responseObject2 = response2.readEntity(ToolTests.class);
        assertEquals(200, response2.getStatus());
        assertEquals("nestedPotato", responseObject2.getTest());
    }

    /**
     * Tests GET /tools/{id}/versions/{version_id}/{type}/descriptor/{relative_path} with:
     * Tool with a dockerfile
     * @throws Exception
     */
    @Test
    public void relativePathEndpointToolContainerfile() {
        Response response = checkedResponse(
            basePath + "tools/quay.io%2Ftest_org%2Ftest6/versions/fakeName/PLAIN_CWL/descriptor/%2FDockerfile");
        String responseObject = response.readEntity(String.class);
        assertEquals(200, response.getStatus());
        assertEquals("potato", responseObject);
    }

    /**
     * Tests PLAIN GET /tools/{id}/versions/{version_id}/{type}/descriptor/{relative_path} with:
     * Workflow with nested cwl test parameter file
     * Workflow with non-existent wdl test parameter file
     * Workflow with non-nested cwl test parameter file
     * @throws Exception
     */
    @Test
    public void relativePathEndpointWorkflowTestParameterFilePLAIN() throws Exception {
        // Insert the 4 workflows into the database using migrations
        CommonTestUtilities.setupTestWorkflow(SUPPORT);

        // Check responses
        Response response = checkedResponse(basePath + "tools/%23workflow%2Fgithub.com%2Fgaryluu%2FtestWorkflow/versions/master/PLAIN_CWL/descriptor/%2Fnested%2Ftest.cwl.json");
        String responseObject = response.readEntity(String.class);
        assertEquals(200, response.getStatus());
        assertEquals("nestedPotato", responseObject);
        Response response2 = client.target(basePath + "tools/quay.io%2Ftest_org%2Ftest6/versions/fakeName/PLAIN_WDL/descriptor/%2Ftest.potato.json").request().get();
        assertEquals(404, response2.getStatus());
        Response response3 = checkedResponse(basePath + "tools/%23workflow%2Fgithub.com%2Fgaryluu%2FtestWorkflow/versions/master/PLAIN_CWL/descriptor/%2Ftest.cwl.json");
        String responseObject3 = response3.readEntity(String.class);
        assertEquals(200, response3.getStatus());
        assertEquals("potato", responseObject3);
    }

    /**
     * Tests JSON GET /tools/{id}/versions/{version_id}/{type}/descriptor/{relative_path} with:
     * Workflow with nested cwl test parameter file
     * Workflow with non-existent wdl test parameter file
     * Workflow with non-nested cwl test parameter file
     * @throws Exception
     */
    @Test
    public void relativePathEndpointWorkflowTestParameterFileJSON() throws Exception {
        // Insert the 4 workflows into the database using migrations
        CommonTestUtilities.setupTestWorkflow(SUPPORT);

        // Check responses
        Response response = checkedResponse(basePath + "tools/%23workflow%2Fgithub.com%2Fgaryluu%2FtestWorkflow/versions/master/CWL/descriptor/%2Fnested%2Ftest.cwl.json");
        ToolTests responseObject = response.readEntity(ToolTests.class);
        assertEquals(200, response.getStatus());
        assertEquals("nestedPotato", responseObject.getTest());
        Response response2 = client.target(basePath + "tools/quay.io%2Ftest_org%2Ftest6/versions/fakeName/WDL/descriptor/%2Ftest.potato.json").request().get();
        assertEquals(404, response2.getStatus());
        Response response3 = checkedResponse(basePath + "tools/%23workflow%2Fgithub.com%2Fgaryluu%2FtestWorkflow/versions/master/CWL/descriptor/%2Ftest.cwl.json");
        ToolTests responseObject3 = response3.readEntity(ToolTests.class);
        assertEquals(200, response3.getStatus());
        assertEquals("potato", responseObject3.getTest());
    }

    private void toolsIdVersionsVersionIdTypeDescriptorRelativePathMissingSlash() throws Exception {
        Response response = checkedResponse(basePath + "tools/quay.io%2Ftest_org%2Ftest6/versions/fakeName/CWL/descriptor/Dockstore.cwl");
        ToolDescriptor responseObject = response.readEntity(ToolDescriptor.class);
        assertThat(response.getStatus()).isEqualTo(200);
        assertDescriptor(MAPPER.writeValueAsString(responseObject));
    }

    private void toolsIdVersionsVersionIdTypeDescriptorRelativePathExtraDot() throws Exception {
        Response response = checkedResponse(
                basePath + "tools/quay.io%2Ftest_org%2Ftest6/versions/fakeName/CWL/descriptor/.%2FDockstore.cwl");
        ToolDescriptor responseObject = response.readEntity(ToolDescriptor.class);
        assertThat(response.getStatus()).isEqualTo(200);
        assertDescriptor(MAPPER.writeValueAsString(responseObject));
    }

    /**
     * This tests the /tools/{id}/versions/{version_id}/{type}/tests endpoint
     *
     * @throws Exception
     */
    @Test
    public void toolsIdVersionsVersionIdTypeTests() throws Exception {
        Response response = checkedResponse(basePath + "tools/quay.io%2Ftest_org%2Ftest6/versions/fakeName/CWL/tests");
        List<ToolTests> responseObject = response.readEntity(new GenericType<List<ToolTests>>() {
        });
        assertThat(MAPPER.writeValueAsString(responseObject).contains("test"));

        assertThat(response.getStatus()).isEqualTo(200);
    }

    void assertDescriptor(String descriptor) {
        assertThat(descriptor).contains("type");
        assertThat(descriptor).contains("descriptor");
    }

    protected abstract void assertTool(String tool, boolean isTool);

    public abstract void toolsIdVersionsVersionIdTypeDockerfile() throws Exception;

    protected abstract void assertVersion(String toolVersion);

    Response checkedResponse(String path) {
        Response response = client.target(path).request().get();
        assertThat(response.getStatus()).isEqualTo(200);
        return response;
    }

    /**
     * This tests that a 400 response returns an Error response object similar to the 404 response defined in the
     * GA4GH swagger.yaml
     * @throws Exception
     */
    @Test
    public void testInvalidToolId() throws Exception {
        Response response = client.target(basePath + "tools/potato").request().get();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        Error error = response.readEntity(Error.class);
        Assert.assertTrue(error.getMessage().contains("Tool ID should"));
        assertThat(error.getCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
    }
}
