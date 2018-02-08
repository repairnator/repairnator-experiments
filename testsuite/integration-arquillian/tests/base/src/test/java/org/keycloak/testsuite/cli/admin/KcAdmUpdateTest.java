package org.keycloak.testsuite.cli.admin;

import org.junit.Assert;
import org.junit.Test;
import org.keycloak.client.admin.cli.config.FileConfigHandler;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.testsuite.cli.KcAdmExec;
import org.keycloak.testsuite.util.TempFileResource;
import org.keycloak.util.JsonSerialization;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.keycloak.testsuite.cli.KcAdmExec.CMD;
import static org.keycloak.testsuite.cli.KcAdmExec.execute;

/**
 * @author <a href="mailto:mstrukel@redhat.com">Marko Strukelj</a>
 */
public class KcAdmUpdateTest extends AbstractAdmCliTest {

    @Test
    public void testUpdateThoroughly() throws IOException {

        FileConfigHandler handler = initCustomConfigFile();

        try (TempFileResource configFile = new TempFileResource(handler.getConfigFile())) {

            final String realm = "test";

            loginAsUser(configFile.getFile(), serverUrl, realm, "user1", "userpass");


            // create an object so we can update it
            KcAdmExec exe = execute("create clients --config '" + configFile.getName() + "' -o -s clientId=my_client");

            assertExitCodeAndStdErrSize(exe, 0, 0);

            ClientRepresentation client = JsonSerialization.readValue(exe.stdout(), ClientRepresentation.class);

            Assert.assertEquals("enabled", true, client.isEnabled());
            Assert.assertEquals("publicClient", false, client.isPublicClient());
            Assert.assertEquals("bearerOnly", false, client.isBearerOnly());
            Assert.assertTrue("redirectUris is empty", client.getRedirectUris().isEmpty());


            // Merge update
            exe = execute("update clients/" + client.getId() + " --config '" + configFile.getName() + "' -o " +
                    " -s enabled=false -s 'redirectUris=[\"http://localhost:8980/myapp/*\"]'");

            assertExitCodeAndStdErrSize(exe, 0, 0);

            client = JsonSerialization.readValue(exe.stdout(), ClientRepresentation.class);
            Assert.assertEquals("enabled", false, client.isEnabled());
            Assert.assertEquals("redirectUris", Arrays.asList("http://localhost:8980/myapp/*"), client.getRedirectUris());



            // Another merge update - test deleting an attribute, deleting a list item and adding a list item
            exe = execute("update clients/" + client.getId() + " --config '" + configFile.getName() + "' -o -d redirectUris[0] -s webOrigins+=http://localhost:8980/myapp -s webOrigins+=http://localhost:8981/myapp -d webOrigins[0]");

            assertExitCodeAndStdErrSize(exe, 0, 0);

            client = JsonSerialization.readValue(exe.stdout(), ClientRepresentation.class);

            Assert.assertTrue("redirectUris is empty", client.getRedirectUris().isEmpty());
            Assert.assertEquals("webOrigins", Arrays.asList("http://localhost:8981/myapp"), client.getWebOrigins());



            // Another merge update - test nested attributes and setting an attribute using json format
            // TODO KEYCLOAK-3705 Updating protocolMapper config via client registration endpoint has no effect
            /*
            exe = execute("update my_client --config '" + configFile.getName() + "' -o -s 'protocolMappers[0].config.\"id.token.claim\"=false' " +
                    "-s 'protocolMappers[4].config={\"single\": \"true\", \"attribute.nameformat\": \"Basic\", \"attribute.name\": \"Role\"}'");

            assertExitCodeAndStdErrSize(exe, 0, 0);

            client = JsonSerialization.readValue(exe.stdout(), ClientRepresentation.class);
            Assert.assertEquals("protocolMapper[0].config.\"id.token.claim\"", "false", client.getProtocolMappers().get(0).getConfig().get("id.token.claim"));
            Assert.assertEquals("protocolMappers[4].config.single", "true", client.getProtocolMappers().get(4).getConfig().get("single"));
            Assert.assertEquals("protocolMappers[4].config.\"attribute.nameformat\"", "Basic", client.getProtocolMappers().get(4).getConfig().get("attribute.nameformat"));
            Assert.assertEquals("protocolMappers[4].config.\"attribute.name\"", "Role", client.getProtocolMappers().get(4).getConfig().get("attribute.name"));
            */

            // update using oidc format


            // check that using an invalid attribute key is not ignored
            exe = execute("update clients/" + client.getId() + " --nonexisting --config '" + configFile.getName() + "'");

            assertExitCodeAndStreamSizes(exe, 1, 0, 2);
            Assert.assertEquals("error message", "Invalid option: --nonexisting", exe.stderrLines().get(0));
            Assert.assertEquals("try help", "Try '" + CMD + " help update' for more information", exe.stderrLines().get(1));


            // test overwrite from file
            exe = KcAdmExec.newBuilder()
                    .argsLine("update clients/" + client.getId() + " --config '" + configFile.getName() +
                            "' -o  -s clientId=my_client -s 'redirectUris=[\"http://localhost:8980/myapp/*\"]' -f -")
                    .stdin(new ByteArrayInputStream("{ \"enabled\": false }".getBytes()))
                    .execute();

            assertExitCodeAndStdErrSize(exe, 0, 0);

            client = JsonSerialization.readValue(exe.stdout(), ClientRepresentation.class);
            // web origin is not sent to the server, thus it retains the current value
            Assert.assertEquals("webOrigins", Arrays.asList("http://localhost:8981/myapp"), client.getWebOrigins());
            Assert.assertFalse("enabled is false", client.isEnabled());
            Assert.assertEquals("redirectUris", Arrays.asList("http://localhost:8980/myapp/*"), client.getRedirectUris());


            // test using merge with file
            exe = KcAdmExec.newBuilder()
                    .argsLine("update clients/" + client.getId() + " --config '" + configFile.getName() +
                            "' -o -s enabled=true -m -f -")
                    .stdin(new ByteArrayInputStream("{ \"webOrigins\": [\"http://localhost:8980/myapp\"] }".getBytes()))
                    .execute();

            assertExitCodeAndStdErrSize(exe, 0, 0);


            client = JsonSerialization.readValue(exe.stdout(), ClientRepresentation.class);
            Assert.assertEquals("webOrigins", Arrays.asList("http://localhost:8980/myapp"), client.getWebOrigins());
            Assert.assertTrue("enabled is true", client.isEnabled());
            Assert.assertEquals("redirectUris", Arrays.asList("http://localhost:8980/myapp/*"), client.getRedirectUris());
        }
    }
}
