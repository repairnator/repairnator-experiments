/*
 * Copyright (c) 2010-2018. Axon Framework
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

package org.axonframework.eventhandling.scheduling.java;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.GenericEventMessage;
import org.axonframework.eventhandling.saga.Saga;
import org.axonframework.eventhandling.scheduling.ScheduleToken;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.quartz.SchedulerException;

import java.io.*;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author Allard Buijze
 * @author Nakul Mishra
 */
public class SimpleEventSchedulerTest {

    private SimpleEventScheduler testSubject;
    private EventBus eventBus;
    private ScheduledExecutorService executorService;

    @Before
    public void setUp() {
        eventBus = mock(EventBus.class);
        executorService = Executors.newSingleThreadScheduledExecutor();
        testSubject = new SimpleEventScheduler(executorService, eventBus);
    }

    @After
    public void tearDown() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }

    @Test
    public void testScheduleJob() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(eventBus).publish(isA(EventMessage.class));
        Saga mockSaga = mock(Saga.class);
        when(mockSaga.getSagaIdentifier()).thenReturn(UUID.randomUUID().toString());
        testSubject.schedule(Duration.ofMillis(30), new Object());
        latch.await(1, TimeUnit.SECONDS);
        verify(eventBus).publish(isA(EventMessage.class));
    }

    @Test
    public void testScheduleTokenIsSerializable() throws IOException, ClassNotFoundException {
        ScheduleToken token = testSubject.schedule(Duration.ZERO, new Object());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(token);
        oos.close();
        ScheduleToken token2 = (ScheduleToken) new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))
                .readObject();
        testSubject.cancelSchedule(token2);
    }

    @Test
    public void testCancelJob() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(eventBus).publish(isA(EventMessage.class));
        Saga mockSaga = mock(Saga.class);
        when(mockSaga.getSagaIdentifier()).thenReturn(UUID.randomUUID().toString());
        EventMessage<Object> event1 = createEvent();
        final EventMessage<Object> event2 = createEvent();
        ScheduleToken token1 = testSubject.schedule(Duration.ofMillis(100), event1);
        testSubject.schedule(Duration.ofMillis(120), event2);
        testSubject.cancelSchedule(token1);
        latch.await(1, TimeUnit.SECONDS);
        verify(eventBus, never()).publish(event1);
        verify(eventBus).publish(argThat((ArgumentMatcher<EventMessage>) item -> (item != null)
                && event2.getPayload().equals(item.getPayload())
                && event2.getMetaData().equals(item.getMetaData())));
        executorService.shutdown();
        assertTrue("Executor refused to shutdown within a second",
                   executorService.awaitTermination(1, TimeUnit.SECONDS));
    }

    private EventMessage<Object> createEvent() {
        return new GenericEventMessage<>(new Object());
    }
}
