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
import org.eclipse.ditto.signals.commands.things.exceptions.FeaturePropertiesNotAccessibleException;
import org.eclipse.ditto.signals.commands.things.exceptions.FeaturePropertiesNotModifiableException;
import org.eclipse.ditto.signals.commands.things.modify.DeleteFeaturePropertiesResponse;
import org.eclipse.ditto.signals.events.base.Event;
import org.eclipse.ditto.signals.events.things.FeaturePropertiesDeleted;

/**
 * A mutable builder with a fluent API for creating a {@link LiveCommandAnswer} for a {@link
 * DeleteFeaturePropertiesLiveCommand}.
 */
@ParametersAreNonnullByDefault
@NotThreadSafe
final class DeleteFeaturePropertiesLiveCommandAnswerBuilderImpl
        extends
        AbstractLiveCommandAnswerBuilder<DeleteFeaturePropertiesLiveCommand, DeleteFeaturePropertiesLiveCommandAnswerBuilder.ResponseFactory, DeleteFeaturePropertiesLiveCommandAnswerBuilder.EventFactory>
        implements DeleteFeaturePropertiesLiveCommandAnswerBuilder {

    private DeleteFeaturePropertiesLiveCommandAnswerBuilderImpl(final DeleteFeaturePropertiesLiveCommand command) {
        super(command);
    }

    /**
     * Returns a new instance of {@code DeleteFeaturePropertiesLiveCommandAnswerBuilderImpl}.
     *
     * @param command the command to build an answer for.
     * @return the instance.
     * @throws NullPointerException if {@code command} is {@code null}.
     */
    public static DeleteFeaturePropertiesLiveCommandAnswerBuilderImpl newInstance(
            final DeleteFeaturePropertiesLiveCommand command) {
        return new DeleteFeaturePropertiesLiveCommandAnswerBuilderImpl(command);
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
        public DeleteFeaturePropertiesResponse deleted() {
            return DeleteFeaturePropertiesResponse.of(command.getThingId(), command.getFeatureId(),
                    command.getDittoHeaders());
        }

        @Nonnull
        @Override
        public ThingErrorResponse featurePropertiesNotAccessibleError() {
            return errorResponse(command.getThingId(),
                    FeaturePropertiesNotAccessibleException.newBuilder(command.getThingId(),
                            command.getFeatureId())
                            .dittoHeaders(command.getDittoHeaders())
                            .build());
        }

        @Nonnull
        @Override
        public ThingErrorResponse featurePropertiesNotModifiableError() {
            return errorResponse(command.getThingId(),
                    FeaturePropertiesNotModifiableException.newBuilder(command.getThingId(),
                            command.getFeatureId())
                            .dittoHeaders(command.getDittoHeaders())
                            .build());
        }
    }

    private final class EventFactoryImpl implements EventFactory {

        @Nonnull
        @Override
        public FeaturePropertiesDeleted deleted() {
            return FeaturePropertiesDeleted.of(command.getThingId(), command.getFeatureId(), -1,
                    Instant.now(), command.getDittoHeaders());
        }
    }

}
