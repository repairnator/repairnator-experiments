package io.swagger.properties;

import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.Operation;
import io.swagger.models.Response;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class MapPropertyDeserializerTest {
  private static final String json = "{" +
      "  \"tags\": [\"store\"]," +
      "  \"summary\": \"Returns pet inventories by status\"," +
      "  \"description\": \"Returns a map of status codes to quantities\"," +
      "  \"operationId\": \"getInventory\"," +
      "  \"produces\": [\"application/json\"]," +
      "  \"parameters\": []," +
      "  \"responses\": {" +
      "    \"200\": {" +
      "      \"description\": \"successful operation\"," +
      "      \"schema\": {" +
      "        \"type\": \"object\"," +
      "        \"x-foo\": \"vendor x\"," +
      "        \"additionalProperties\": {" +
      "          \"type\": \"integer\"," +
      "          \"format\": \"int32\"" +
      "        }" +
      "      }" +
      "    }" +
      "  }," +
      "  \"security\": [{" +
      "    \"api_key\": []" +
      "  }]" +
      "}";

  @Test(description = "it should deserialize a response per #1349")
  public void testMapDeserialization () throws Exception {

      Operation operation = Json.mapper().readValue(json, Operation.class);
      Response response = operation.getResponses().get("200");
      assertNotNull(response);
      
      Model responseSchema = response.getResponseSchema();
      assertNotNull(responseSchema);
      assertTrue(responseSchema instanceof ModelImpl);

      ModelImpl modelImpl = (ModelImpl) responseSchema;
      assertTrue(modelImpl.getAdditionalProperties() instanceof IntegerProperty);
  }

  @Test(description = "vendor extensions should be included with object type")
  public void testMapDeserializationVendorExtensions () throws Exception {
    Operation operation = Json.mapper().readValue(json, Operation.class);
    Response response = operation.getResponses().get("200");
    assertNotNull(response);

    Model responseSchema = response.getResponseSchema();
    assertNotNull(responseSchema);

    ModelImpl modelImpl = (ModelImpl) responseSchema;
    assertTrue(modelImpl.getVendorExtensions().size() > 0);
    assertNotNull(modelImpl.getVendorExtensions().get("x-foo"));
    assertEquals(modelImpl.getVendorExtensions().get("x-foo"), "vendor x");
  }

    @Test(description = "it should read an example within an inlined schema")
    public void testIssue1261InlineSchemaExample() throws Exception {
        String yaml = "      produces:\n" +
               "        - application/json\n" +
               "      parameters:\n" +
               "        []\n" +
               "      responses:\n" +
               "        200:\n" +
               "          description: OK\n" +
               "          schema:\n" +
               "            type: object\n" +
               "            properties:\n" +
               "              id:\n" +
               "                type: integer\n" +
               "                format: int32\n" +
               "              name:\n" +
               "                type: string\n" +
               "            required: [id, name]\n" +
               "            example:\n" +
               "              id: 42\n" +
               "              name: Arthur Dent\n";

        Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
        Response response = operation.getResponses().get("200");
        assertNotNull(response);
        Model schema = response.getResponseSchema();
        Object example = schema.getExample();
        assertNotNull(example);
        assertTrue(example instanceof LinkedHashMap);
        LinkedHashMap exampleMap = (LinkedHashMap) example;
        assertEquals(exampleMap.get("id"), 42);
        assertEquals(exampleMap.get("name"), "Arthur Dent");
        SerializationMatchers.assertEqualsToYaml(operation, yaml);
    }
}