/*
 * Copyright (c) 2017 Bosch Software Innovations GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/index.php
 *
 * Contributors:
 *    Bosch Software Innovations GmbH - initial contribution
 */
package org.eclipse.ditto.signals.commands.live.modify;

import java.time.Instant;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.ditto.signals.commands.base.CommandResponse;
import org.eclipse.ditto.signals.commands.live.base.LiveCommandAnswer;
import org.eclipse.ditto.signals.commands.things.ThingErrorResponse;
import org.eclipse.ditto.signals.commands.things.exceptions.FeaturePropertyNotAccessibleException;
import org.eclipse.ditto.signals.commands.things.exceptions.FeaturePropertyNotModifiableException;
import org.eclipse.ditto.signals.commands.things.modify.DeleteFeaturePropertyResponse;
import org.eclipse.ditto.signals.events.base.Event;
import org.eclipse.ditto.signals.events.things.FeaturePropertyDeleted;

/**
 * A mutable builder with a fluent API for creating a {@link LiveCommandAnswer} for a {@link
 * DeleteFeaturePropertyLiveCommand}.
 */
@ParametersAreNonnullByDefault
@NotThreadSafe
final class DeleteFeaturePropertyLiveCommandAnswerBuilderImpl
        extends
        AbstractLiveCommandAnswerBuilder<DeleteFeaturePropertyLiveCommand, DeleteFeaturePropertyLiveCommandAnswerBuilder.ResponseFactory, DeleteFeaturePropertyLiveCommandAnswerBuilder.EventFactory>
        implements DeleteFeaturePropertyLiveCommandAnswerBuilder {

    private DeleteFeaturePropertyLiveCommandAnswerBuilderImpl(final DeleteFeaturePropertyLiveCommand command) {
        super(command);
    }

    /**
     * Returns a new instance of {@code DeleteFeaturePropertyLiveCommandAnswerBuilderImpl}.
     *
     * @param command the command to build an answer for.
     * @return the instance.
     * @throws NullPointerException if {@code command} is {@code null}.
     */
    public static DeleteFeaturePropertyLiveCommandAnswerBuilderImpl newInstance(
            final DeleteFeaturePropertyLiveCommand command) {
        return new DeleteFeaturePropertyLiveCommandAnswerBuilderImpl(command);
    }

    @Override
    protected CommandResponse doCreateResponse(
            final Function<ResponseFactory, CommandResponse<?>> createResponseFunction) {
        return createResponseFunction.apply(new ResponseFactoryImpl());
    }

    @Override
    protected Event doCreateEvent(final Function<EventFactory, Event<?>> createEventFunction) {
        return createEventFunction.apply(new EventFactoryImpl());
    }

    @Immutable
    private final class ResponseFactoryImpl implements ResponseFactory {

        @Nonnull
        @Override
        public DeleteFeaturePropertyResponse deleted() {
            return DeleteFeaturePropertyResponse.of(command.getThingId(), command.getFeatureId(),
                    command.getPropertyPointer(),
                    command.getDittoHeaders());
        }

        @Nonnull
        @Override
        public ThingErrorResponse featurePropertyNotAccessibleError() {
            return errorResponse(command.getThingId(),
                    FeaturePropertyNotAccessibleException.newBuilder(command.getThingId(),
                            command.getFeatureId(),
                            command.getPropertyPointer())
                            .dittoHeaders(command.getDittoHeaders())
                            .build());
        }

        @Nonnull
        @Override
        public ThingErrorResponse featurePropertyNotModifiableError() {
            return errorResponse(command.getThingId(),
                    FeaturePropertyNotModifiableException.newBuilder(command.getThingId(),
                            command.getFeatureId(),
                            command.getPropertyPointer())
                            .dittoHeaders(command.getDittoHeaders())
                            .build());
        }
    }

    @Immutable
    private final class EventFactoryImpl implements EventFactory {

        @Nonnull
        @Override
        public FeaturePropertyDeleted deleted() {
            return FeaturePropertyDeleted.of(command.getThingId(), command.getFeatureId(),
                    command.getPropertyPointer(), -1, Instant.now(), command.getDittoHeaders());
        }
    }

}
