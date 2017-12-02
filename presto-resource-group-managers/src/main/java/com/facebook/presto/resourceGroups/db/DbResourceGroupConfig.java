/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.resourceGroups.db;

import io.airlift.configuration.Config;

public class DbResourceGroupConfig
{
    private String configUrl;
    private boolean exactMatchSelectorEnabled;

    public String getConfigDbUrl()
    {
        return configUrl;
    }

    @Config("resource-groups.config-db-url")
    public DbResourceGroupConfig setConfigDbUrl(String configUrl)
    {
        this.configUrl = configUrl;
        return this;
    }

    public boolean getExactMatchSelectorEnabled()
    {
        return exactMatchSelectorEnabled;
    }

    @Config("resource-groups.exact-match-selector-enabled")
    public DbResourceGroupConfig setExactMatchSelectorEnabled(boolean exactMatchSelectorEnabled)
    {
        this.exactMatchSelectorEnabled = exactMatchSelectorEnabled;
        return this;
    }
}
