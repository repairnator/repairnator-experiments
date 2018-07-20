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
package org.eclipse.ditto.signals.commands.things.exceptions;

import java.net.URI;
import java.text.MessageFormat;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.ditto.json.JsonObject;
import org.eclipse.ditto.model.base.common.HttpStatusCode;
import org.eclipse.ditto.model.base.exceptions.DittoRuntimeException;
import org.eclipse.ditto.model.base.exceptions.DittoRuntimeExceptionBuilder;
import org.eclipse.ditto.model.base.headers.DittoHeaders;
import org.eclipse.ditto.model.things.ThingException;

/**
 * Thrown if the Thing with its ID is already present in Ditto.
 */
@Immutable
public final class ThingConflictException extends DittoRuntimeException implements ThingException {

    /**
     * Error code of this exception.
     */
    public static final String ERROR_CODE = ERROR_CODE_PREFIX + "thing.conflict";

    private static final String MESSAGE_TEMPLATE = "The Thing with ID ''{0}'' already exists";

    private static final String DEFAULT_DESCRIPTION = "Choose another Thing ID";

    private static final long serialVersionUID = -5987230030980819468L;

    private ThingConflictException(final DittoHeaders dittoHeaders, final String message, final String description,
            final Throwable cause, final URI href) {
        super(ERROR_CODE, HttpStatusCode.CONFLICT, dittoHeaders, message, description, cause, href);
    }

    /**
     * A mutable builder for a {@code ThingConflictException}.
     *
     * @param thingId the ID of the thing.
     * @return the builder.
     */
    public static Builder newBuilder(final String thingId) {
        return new Builder(thingId);
    }

    /**
     * Constructs a new {@code ThingConflictException} object with given message.
     *
     * @param message detail message. This message can be later retrieved by the {@link #getMessage()} method.
     * @param dittoHeaders the headers of the command which resulted in this exception.
     * @return the new ThingConflictException.
     */
    public static ThingConflictException fromMessage(final String message, final DittoHeaders dittoHeaders) {
        return new Builder()
                .dittoHeaders(dittoHeaders)
                .message(message)
                .build();
    }

    /**
     * Constructs a new {@code ThingConflictException} object with the exception message extracted from the
     * given JSON object.
     *
     * @param jsonObject the JSON to read the {@link JsonFields#MESSAGE} field from.
     * @param dittoHeaders the headers of the command which resulted in this exception.
     * @return the new ThingConflictException.
     * @throws org.eclipse.ditto.json.JsonMissingFieldException if the {@code jsonObject} does not have the {@link
     * JsonFields#MESSAGE} field.
     */
    public static ThingConflictException fromJson(final JsonObject jsonObject, final DittoHeaders dittoHeaders) {
        return fromMessage(readMessage(jsonObject), dittoHeaders);
    }

    /**
     * A mutable builder with a fluent API for a {@link ThingConflictException}.
     *
     */
    @NotThreadSafe
    public static final class Builder extends DittoRuntimeExceptionBuilder<ThingConflictException> {

        private Builder() {
            description(DEFAULT_DESCRIPTION);
        }

        private Builder(final String thingId) {
            this();
            message(MessageFormat.format(MESSAGE_TEMPLATE, thingId));
        }

        @Override
        protected ThingConflictException doBuild(final DittoHeaders dittoHeaders, final String message,
                final String description, final Throwable cause, final URI href) {
            return new ThingConflictException(dittoHeaders, message, description, cause, href);
        }
    }

}
