/*
 * Copyright 2018-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.core.profile;

import java.util.Collection;

/**
 * Profile type registry.
 */
public interface ProfileRegistry {

  /**
   * Returns the collection of all registered profiles.
   *
   * @return the collection of all registered profiles
   */
  Collection<Profile> getProfiles();

  /**
   * Returns the profile for the given name.
   *
   * @param name the profile name
   * @return the profile
   */
  Profile getProfile(String name);

}
