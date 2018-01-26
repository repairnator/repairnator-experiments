/*
 * Copyright (c) 2010-2014. Axon Framework
 *
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

package org.axonframework.eventsourcing.eventstore.jpa;

import org.axonframework.eventsourcing.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.AbstractSnapshotEventEntry;
import org.axonframework.serialization.Serializer;

import javax.persistence.Entity;

/**
 * @author Allard Buijze
 */
@Entity
public class CustomSnapshotEventEntry extends AbstractSnapshotEventEntry<String> {

    public CustomSnapshotEventEntry(DomainEventMessage event, Serializer serializer) {
        super(event, serializer, String.class);
    }

    /**
     * Default constructor required by JPA
     */
    protected CustomSnapshotEventEntry() {
    }
}
