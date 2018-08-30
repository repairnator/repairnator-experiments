/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.druid.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import org.apache.druid.java.util.common.config.Config;
import org.skife.config.ConfigurationObjectFactory;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Properties;

/**
 */
public class ConfigModule implements Module
{
  @Override
  public void configure(Binder binder)
  {
    binder.bind(Validator.class).toInstance(Validation.buildDefaultValidatorFactory().getValidator());
    binder.bind(JsonConfigurator.class).in(LazySingleton.class);
  }

  @Provides @LazySingleton
  public ConfigurationObjectFactory makeFactory(Properties props)
  {
    return Config.createFactory(props);
  }
}
