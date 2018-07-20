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

import javax.annotation.Nonnull;

import org.eclipse.ditto.signals.commands.things.ThingErrorResponse;
import org.eclipse.ditto.signals.commands.things.modify.DeleteFeatures;
import org.eclipse.ditto.signals.commands.things.modify.DeleteFeaturesResponse;
import org.eclipse.ditto.signals.events.things.FeaturesDeleted;

import org.eclipse.ditto.signals.commands.live.base.LiveCommandAnswerBuilder;
import org.eclipse.ditto.signals.commands.live.base.LiveCommandResponseFactory;
import org.eclipse.ditto.signals.commands.live.base.LiveEventFactory;

/**
 * LiveCommandAnswer builder for producing {@code CommandResponse}s and {@code Event}s for {@link DeleteFeatures}
 * commands.
 */
public interface DeleteFeaturesLiveCommandAnswerBuilder extends LiveCommandAnswerBuilder.ModifyCommandResponseStep<
        DeleteFeaturesLiveCommandAnswerBuilder.ResponseFactory, DeleteFeaturesLiveCommandAnswerBuilder.EventFactory> {

    /**
     * Factory for {@code CommandResponse}s to {@link DeleteFeatures} command.
     */
    interface ResponseFactory extends LiveCommandResponseFactory {

        /**
         * Builds a {@link DeleteFeaturesResponse} using the values of the {@code Command}.
         *
         * @return the response.
         */
        @Nonnull
        DeleteFeaturesResponse deleted();

        /**
         * Builds a {@link ThingErrorResponse} indicating that the features were not accessible.
         *
         * @return the response.
         * @see org.eclipse.ditto.signals.commands.things.exceptions.FeaturesNotAccessibleException
         * FeaturesNotAccessibleException
         */
        @Nonnull
        ThingErrorResponse featuresNotAccessibleError();

        /**
         * Builds a {@link ThingErrorResponse} indicating that the features were not modifiable.
         *
         * @return the response.
         * @see org.eclipse.ditto.signals.commands.things.exceptions.FeaturesNotModifiableException
         * FeaturesNotModifiableException
         */
        @Nonnull
        ThingErrorResponse featuresNotModifiableError();
    }

    /**
     * Factory for events triggered by {@link DeleteFeatures} command.
     */
    @SuppressWarnings("squid:S1609")
    interface EventFactory extends LiveEventFactory {

        /**
         * Creates a {@link FeaturesDeleted} event using the values of the {@code Command}.
         *
         * @return the event.
         */
        @Nonnull
        FeaturesDeleted deleted();
    }

}
