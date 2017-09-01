/*
 * Licensed to Metamarkets Group Inc. (Metamarkets) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Metamarkets licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.druid.security.kerberos;

import org.junit.Test;

public class AuthenticationKerberosConfigTest
{
  @Test
  public void testserde()
  {
    /*
    Injector injector = Guice.createInjector(
      new Module()
      {
        @Override
        public void configure(Binder binder)
        {
          binder.install(new PropertiesModule(Arrays.asList("test.runtime.properties")));
          binder.install(new ConfigModule());
          binder.install(new DruidGuiceExtensions());
          JsonConfigProvider.bind(binder, "druid.hadoop.security.kerberos", AuthenticationKerberosConfig.class);
        }

        @Provides
        @LazySingleton
        public ObjectMapper jsonMapper()
        {
          return new DefaultObjectMapper();
        }
      }
    );

    Properties props = injector.getInstance(Properties.class);
    AuthenticationKerberosConfig config = injector.getInstance(AuthenticationKerberosConfig.class);

    Assert.assertEquals(props.getProperty("druid.hadoop.security.kerberos.principal"), config.getPrincipal());
    Assert.assertEquals(props.getProperty("druid.hadoop.security.kerberos.keytab"), config.getKeytab());
    */
  }
}
