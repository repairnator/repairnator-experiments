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
package com.facebook.presto.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.airlift.units.Duration;

import javax.annotation.concurrent.Immutable;

import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

@Immutable
public class ServerInfo
{
    private final NodeVersion nodeVersion;
    private final String environment;
    private final boolean coordinator;

    // optional to maintain compatibility with older servers
    private final Optional<Duration> uptime;

    @JsonCreator
    public ServerInfo(
            @JsonProperty("nodeVersion") NodeVersion nodeVersion,
            @JsonProperty("environment") String environment,
            @JsonProperty("coordinator") boolean coordinator,
            @JsonProperty("uptime") Optional<Duration> uptime)
    {
        this.nodeVersion = requireNonNull(nodeVersion, "nodeVersion is null");
        this.environment = requireNonNull(environment, "environment is null");
        this.coordinator = requireNonNull(coordinator, "coordinator is null");
        this.uptime = requireNonNull(uptime, "uptime is null");
    }

    @JsonProperty
    public NodeVersion getNodeVersion()
    {
        return nodeVersion;
    }

    @JsonProperty
    public String getEnvironment()
    {
        return environment;
    }

    @JsonProperty
    public boolean isCoordinator()
    {
        return coordinator;
    }

    @JsonProperty
    public Optional<Duration> getUptime()
    {
        return uptime;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServerInfo that = (ServerInfo) o;
        return Objects.equals(nodeVersion, that.nodeVersion) &&
                Objects.equals(environment, that.environment);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(nodeVersion, environment);
    }

    @Override
    public String toString()
    {
        return toStringHelper(this)
                .add("nodeVersion", nodeVersion)
                .add("environment", environment)
                .add("coordinator", coordinator)
                .add("uptime", uptime.orElse(null))
                .omitNullValues()
                .toString();
    }
}
