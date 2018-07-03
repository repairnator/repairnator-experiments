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

package org.axonframework.test.saga;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.deadline.DeadlineMessage;
import org.axonframework.eventhandling.EventMessage;
import org.hamcrest.Matcher;

import java.time.Duration;
import java.time.Instant;
import java.util.List;


/**
 * Interface towards an object that contains the results of a Fixture execution. It provides methods to assert that
 * certain actions have taken place.
 *
 * @author Allard Buijze
 * @since 1.1
 */
public interface FixtureExecutionResult {

    /**
     * Expect the given number of Sagas to be active (i.e. ready to respond to incoming events.
     *
     * @param expected the expected number of active events in the repository
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectActiveSagas(int expected);

    /**
     * Asserts that at least one of the active sagas is associated with the given {@code associationKey} and
     * {@code associationValue}.
     *
     * @param associationKey   The key of the association to verify
     * @param associationValue The value of the association to verify
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectAssociationWith(String associationKey, Object associationValue);

    /**
     * Asserts that at none of the active sagas is associated with the given {@code associationKey} and
     * {@code associationValue}.
     *
     * @param associationKey   The key of the association to verify
     * @param associationValue The value of the association to verify
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectNoAssociationWith(String associationKey, Object associationValue);

    /**
     * Asserts that an event matching the given {@code matcher} has been scheduled to be published after the given
     * {@code duration}.
     *
     * @param duration The time to wait before the event should be published
     * @param matcher  A matcher defining the event expected to be published
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectScheduledEventMatching(Duration duration, Matcher<? super EventMessage<?>> matcher);

    /**
     * Asserts that a deadline scheduled after given {@code duration} matches the given {@code matcher}.
     *
     * @param duration The delay expected before the deadline is met
     * @param matcher  The matcher that must match with the deadline scheduled at the given time
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectScheduledDeadlineMatching(Duration duration,
                                                           Matcher<? super DeadlineMessage<?>> matcher);

    /**
     * Asserts that an event equal to the given ApplicationEvent has been scheduled for publication after the given
     * {@code duration}.
     * <p/>
     * Note that the source attribute of the application event is ignored when comparing events. Events are compared
     * using an "equals" check on all fields in the events.
     *
     * @param duration The time to wait before the event should be published
     * @param event    The expected event
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectScheduledEvent(Duration duration, Object event);

    /**
     * Asserts that a deadline equal to the given {@code deadline} has been scheduled after the given {@code duration}.
     * <p/>
     * Note that the source attribute of the deadline is ignored when comparing deadlines. Deadlines are compared using
     * an "equals" check on all fields in the deadlines.
     *
     * @param duration The time to wait before the deadline should be met
     * @param deadline The expected deadline
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectScheduledDeadline(Duration duration, Object deadline);

    /**
     * Asserts that an event of the given {@code eventType} has been scheduled for publication after the given
     * {@code duration}.
     *
     * @param duration  The time to wait before the event should be published
     * @param eventType The type of the expected event
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectScheduledEventOfType(Duration duration, Class<?> eventType);

    /**
     * Asserts that a deadline of the given {@code deadlineType} has been scheduled after the given {@code duration}.
     *
     * @param duration     The time to wait before the deadline is met
     * @param deadlineType The type of the expected deadline
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectScheduledDeadlineOfType(Duration duration, Class<?> deadlineType);

    /**
     * Asserts that an event matching the given {@code matcher} has been scheduled to be published at the given
     * {@code scheduledTime}.
     * <p/>
     * If the {@code scheduledTime} is calculated based on the "current time", use the {@link
     * org.axonframework.test.saga.FixtureConfiguration#currentTime()} to get the time to use as "current time".
     *
     * @param scheduledTime The time at which the event should be published
     * @param matcher       A matcher defining the event expected to be published
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectScheduledEventMatching(Instant scheduledTime,
                                                        Matcher<? super EventMessage<?>> matcher);

    /**
     * Asserts that a deadline matching the given {@code matcher} has been scheduled at the given {@code
     * scheduledTime}.
     * <p/>
     * If the {@code scheduledTime} is calculated based on the "current time", use the {@link
     * FixtureConfiguration#currentTime()} to get the time to use as "current time".
     *
     * @param scheduledTime The time at which the deadline should be met
     * @param matcher       The matcher defining the deadline expected
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectScheduledDeadlineMatching(Instant scheduledTime,
                                                           Matcher<? super DeadlineMessage<?>> matcher);

    /**
     * Asserts that an event equal to the given ApplicationEvent has been scheduled for publication at the given
     * {@code scheduledTime}.
     * <p/>
     * If the {@code scheduledTime} is calculated based on the "current time", use the {@link
     * org.axonframework.test.saga.FixtureConfiguration#currentTime()} to get the time to use as "current time".
     * <p/>
     * Note that the source attribute of the application event is ignored when comparing events. Events are compared
     * using an "equals" check on all fields in the events.
     *
     * @param scheduledTime The time at which the event should be published
     * @param event         The expected event
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectScheduledEvent(Instant scheduledTime, Object event);

    /**
     * Asserts that a deadline equal to the given {@code deadline} has been scheduled at the given {@code
     * scheduledTime}.
     * <p/>
     * If the {@code scheduledTime} is calculated based on the "current time", use the {@link
     * FixtureConfiguration#currentTime()} to get the time to use as "current time".
     * <p/>
     * Note that the source attribute of the deadline is ignored when comparing deadlines. Deadlines are compared using
     * an "equals" check on all fields in the deadlines.
     *
     * @param scheduledTime The time at which the deadline is scheduled
     * @param deadline      The expected deadline
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectScheduledDeadline(Instant scheduledTime, Object deadline);

    /**
     * Asserts that an event of the given {@code eventType} has been scheduled for publication at the given
     * {@code scheduledTime}.
     * <p/>
     * If the {@code scheduledTime} is calculated based on the "current time", use the {@link
     * org.axonframework.test.saga.FixtureConfiguration#currentTime()} to get the time to use as "current time".
     *
     * @param scheduledTime The time at which the event should be published
     * @param eventType     The type of the expected event
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectScheduledEventOfType(Instant scheduledTime, Class<?> eventType);

    /**
     * Asserts that a deadline of the given {@code deadlineType} has been scheduled at the given {@code scheduledTime}.
     *
     * @param scheduledTime The time at which the deadline is scheduled
     * @param deadlineType  The type of the expected deadline
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectScheduledDeadlineOfType(Instant scheduledTime, Class<?> deadlineType);

    /**
     * Asserts that the given commands have been dispatched in exactly the order given. The command objects are
     * compared using the equals method. Only commands as a result of the event in the "when" stage of the fixture are
     * compared.
     *
     * If exact order doesn't matter, or the validation needs be done in another way than "equal payload", consider
     * using {@link #expectDispatchedCommandsMatching(Matcher)} instead.
     *
     * @param commands The expected commands
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectDispatchedCommands(Object... commands);

    /**
     * Asserts that the sagas dispatched commands as defined by the given {@code matcher}. Only commands as a
     * result of the event in the "when" stage of the fixture are matched.
     *
     * @param matcher The matcher that describes the expected list of commands
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectDispatchedCommandsMatching(Matcher<? extends List<? super CommandMessage<?>>> matcher);

    /**
     * Asserts that the sagas did not dispatch any commands. Only commands as a result of the event in the "when" stage
     * of ths fixture are recorded.
     *
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectNoDispatchedCommands();

    /**
     * Assert that no events are scheduled for publication. This means that either no events were scheduled at all, all
     * schedules have been cancelled or all scheduled events have been published already.
     *
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectNoScheduledEvents();

    /**
     * Asserts that no deadlines are scheduled. This means that either no deadlines were scheduled at all, all schedules
     * have been cancelled or all scheduled deadlines have been met already.
     *
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectNoScheduledDeadlines();

    /**
     * Assert that the saga published events on the EventBus as defined by the given {@code matcher}. Only events
     * published in the "when" stage of the tests are matched.
     *
     * @param matcher The matcher that defines the expected list of published events.
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectPublishedEventsMatching(Matcher<? extends List<? super EventMessage<?>>> matcher);

    /**
     * Asserts that deadlines match given {@code matcher} have been met (which have passed in time) on this saga.
     *
     * @param matcher The matcher that defines the expected list of deadlines
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectDeadlinesMetMatching(Matcher<? extends List<? super DeadlineMessage<?>>> matcher);

    /**
     * Assert that the saga published events on the EventBus in the exact sequence of the given {@code expected}
     * events. Events are compared comparing their type and fields using equals. Sequence number, aggregate identifier
     * (for domain events) and source (for application events) are ignored in the comparison.
     *
     * @param expected The sequence of events expected to be published by the Saga
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectPublishedEvents(Object... expected);

    /**
     * Asserts that given {@code expected} deadlines have been met (which have passed in time). Deadlines are compared
     * comparing their type and fields using "equals".
     *
     * @param expected The sequence of deadlines expected to be met
     * @return the FixtureExecutionResult for method chaining
     */
    FixtureExecutionResult expectDeadlinesMet(Object... expected);
}
