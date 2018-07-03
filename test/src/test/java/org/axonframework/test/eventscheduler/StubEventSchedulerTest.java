/*
 * Copyright (c) 2010-2012. Axon Framework
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

package org.axonframework.test.eventscheduler;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.GenericEventMessage;
import org.junit.*;
import org.junit.rules.*;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;

/**
 * @author Allard Buijze
 */
public class StubEventSchedulerTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private StubEventScheduler testSubject;

    @Before
    public void setUp() {
        testSubject = new StubEventScheduler();
    }

    @Test
    public void testScheduleEvent() {
        testSubject.schedule(Instant.now().plus(Duration.ofDays(1)), event(new MockEvent()));
        assertEquals(1, testSubject.getScheduledItems().size());
    }

    @Test
    public void testInitializeAtDateTimeAfterSchedulingEvent() {
        testSubject.schedule(Instant.now().plus(Duration.ofDays(1)), event(new MockEvent()));
        exception.expect(IllegalStateException.class);
        testSubject.initializeAt(Instant.now().minus(10, ChronoUnit.MINUTES));
    }

    private EventMessage<MockEvent> event(MockEvent mockEvent) {
        return new GenericEventMessage<>(mockEvent);
    }

    private static class MockEvent {

    }
}
