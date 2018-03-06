package eu.europeana.metis.core.test.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class TestUtils {

  private TestUtils() {
  }

  public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return mapper.writeValueAsBytes(object);
  }

  public static boolean untilThreadIsSleeping(Thread t) {
    return "java.lang.Thread".equals(t.getStackTrace()[0].getClassName()) && "sleep"
        .equals(t.getStackTrace()[0].getMethodName());
  }
}
