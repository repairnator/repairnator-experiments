/*
 * Copyright 2018 The Hekate Project
 *
 * The Hekate Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.hekate.cluster.internal.gossip;

import io.hekate.core.internal.util.AddressUtils;
import io.hekate.util.format.ToString;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class GossipSeedNodesSate {
    private enum Status {
        NEW,

        RETRY,

        FAILED,

        BAN
    }

    private static class SeedNodeStatus implements Comparable<SeedNodeStatus> {
        private final InetSocketAddress address;

        private Status status;

        public SeedNodeStatus(InetSocketAddress address) {
            this.address = address;
            this.status = Status.NEW;
        }

        public InetSocketAddress address() {
            return address;
        }

        public Status status() {
            return status;
        }

        public void updateStatus(Status status) {
            this.status = status;
        }

        @Override
        public int compareTo(SeedNodeStatus o) {
            return compare(address, o.address);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof SeedNodeStatus)) {
                return false;
            }

            SeedNodeStatus that = (SeedNodeStatus)o;

            return Objects.equals(address, that.address);
        }

        @Override
        public int hashCode() {
            return Objects.hash(address);
        }

        @Override
        public String toString() {
            return ToString.format(this);
        }
    }

    private final InetSocketAddress localAddress;

    private List<SeedNodeStatus> seeds;

    private InetSocketAddress lastTried;

    public GossipSeedNodesSate(InetSocketAddress localAddress, List<InetSocketAddress> seeds) {
        this.localAddress = localAddress;

        Set<InetSocketAddress> uniqueAddresses = new HashSet<>(seeds);

        uniqueAddresses.add(localAddress);

        this.seeds = uniqueAddresses.stream()
            .map(SeedNodeStatus::new)
            .sorted()
            .collect(Collectors.toList());
    }

    public boolean isSelfJoin() {
        return triedAllNodes() && seeds.stream()
            .filter(s -> s.status() != Status.FAILED && s.status() != Status.BAN)
            .limit(1)
            .anyMatch(s -> s.address().equals(localAddress));
    }

    public InetSocketAddress nextSeed() {
        // TODO: Prefer RETRY nodes if already tried all nodes.

        if (lastTried != null) {
            lastTried = seeds.stream()
                .filter(s -> s.status() != Status.BAN && !s.address().equals(localAddress) && compare(s.address(), lastTried) > 0)
                .findFirst()
                .map(SeedNodeStatus::address)
                .orElse(null);
        }

        if (lastTried == null) {
            lastTried = seeds.stream()
                .filter(s -> s.status() != Status.BAN && !s.address().equals(localAddress))
                .findFirst()
                .map(SeedNodeStatus::address)
                .orElse(null);
        }

        return lastTried;
    }

    public void update(List<InetSocketAddress> newSeeds) {
        Set<InetSocketAddress> uniqueAddresses = new HashSet<>(newSeeds);

        uniqueAddresses.add(localAddress);

        this.seeds = uniqueAddresses.stream()
            // Preserve state of previously checked addresses.
            .map(address -> seeds.stream()
                .filter(s -> s.address().equals(address))
                .findFirst()
                // ...or create new initial state for new addresses.
                .orElse(new SeedNodeStatus(address)))
            .sorted()
            .collect(Collectors.toList());

        if (lastTried != null && seeds.stream().noneMatch(s -> s.address().equals(lastTried))) {
            lastTried = null;
        }
    }

    public void onReject(InetSocketAddress seed) {
        seeds.stream()
            .filter(s -> s.status() != Status.BAN && s.address().equals(seed))
            .findFirst()
            .ifPresent(s -> s.updateStatus(Status.RETRY));
    }

    public void onFailure(InetSocketAddress seed) {
        seeds.stream()
            .filter(s -> s.status() != Status.BAN && s.address().equals(seed))
            .findFirst()
            .ifPresent(s -> s.updateStatus(Status.FAILED));
    }

    public void onBan(InetSocketAddress seed) {
        seeds.stream().filter(s -> s.address().equals(seed))
            .findFirst()
            .ifPresent(s -> s.updateStatus(Status.BAN));
    }

    private boolean triedAllNodes() {
        return seeds.stream().allMatch(s -> s.address().equals(localAddress) || s.status() != Status.NEW);
    }

    private static int compare(InetSocketAddress a1, InetSocketAddress a2) {
        int cmp = AddressUtils.host(a1).compareTo(AddressUtils.host(a2));

        if (cmp == 0) {
            cmp = Integer.compare(a1.getPort(), a2.getPort());
        }

        return cmp;
    }

    @Override
    public String toString() {
        return ToString.format(this);
    }
}
