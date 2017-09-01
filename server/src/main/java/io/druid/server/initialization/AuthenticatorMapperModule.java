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

package io.druid.server.initialization;

import com.fasterxml.jackson.databind.Module;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import io.druid.guice.JsonConfigProvider;
import io.druid.guice.JsonConfigurator;
import io.druid.guice.LazySingleton;
import io.druid.guice.LifecycleModule;
import io.druid.initialization.DruidModule;
import io.druid.java.util.common.ISE;
import io.druid.java.util.common.StringUtils;
import io.druid.java.util.common.logger.Logger;
import io.druid.server.security.AuthConfig;
import io.druid.server.security.Authenticator;
import io.druid.server.security.AuthenticatorMapper;
import io.druid.server.security.AllowAllAuthenticator;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AuthenticatorMapperModule implements DruidModule
{
  private static final String AUTHENTICATOR_PROPERTIES_FORMAT_STRING = "druid.auth.authenticator.%s";
  private static Logger log = new Logger(AuthenticatorMapperModule.class);

  @Override
  public void configure(Binder binder)
  {
    binder.bind(AuthenticatorMapper.class)
          .toProvider(new AuthenticatorMapperProvider())
          .in(LazySingleton.class);

    LifecycleModule.register(binder, AuthenticatorMapper.class);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<? extends Module> getJacksonModules()
  {
    return Collections.EMPTY_LIST;
  }

  private static class AuthenticatorMapperProvider implements Provider<AuthenticatorMapper>
  {
    private AuthConfig authConfig;
    private Injector injector;
    private Properties props;
    private JsonConfigurator configurator;

    @Inject
    public void inject(Injector injector, Properties props, JsonConfigurator configurator)
    {
      this.authConfig = injector.getInstance(AuthConfig.class);
      this.injector = injector;
      this.props = props;
      this.configurator = configurator;
    }

    @Override
    public AuthenticatorMapper get()
    {
      // order of the authenticators matters
      Map<String, Authenticator> authenticatorMap = Maps.newLinkedHashMap();

      List<String> authenticators = authConfig.getAuthenticatorChain();

      // If user didn't configure any Authenticators, use the default which accepts all requests.
      if (authenticators == null || authenticators.isEmpty()) {
        Map<String, Authenticator> defaultMap = Maps.newHashMap();
        defaultMap.put("allowAll", new AllowAllAuthenticator());
        return new AuthenticatorMapper(defaultMap, "allowAll");
      }

      for (String authenticatorName : authenticators) {
        final String authenticatorPropertyBase = StringUtils.format(AUTHENTICATOR_PROPERTIES_FORMAT_STRING, authenticatorName);
        final JsonConfigProvider<Authenticator> authenticatorProvider = new JsonConfigProvider<>(
            authenticatorPropertyBase,
            Authenticator.class
        );

        authenticatorProvider.inject(props, configurator);

        Supplier<Authenticator> authenticatorSupplier = authenticatorProvider.get();
        if (authenticatorSupplier == null) {
          throw new ISE("Could not create authenticator with name: %s", authenticatorName);
        }
        Authenticator authenticator = authenticatorSupplier.get();
        authenticatorMap.put(authenticatorName, authenticator);
      }

      return new AuthenticatorMapper(authenticatorMap, authConfig.getEscalatedAuthenticator());
    }
  }
}
