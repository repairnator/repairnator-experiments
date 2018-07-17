/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 1.0 which is available at
 * https://www.eclipse.org/legal/epl-v10.html
 *
 * SPDX-License-Identifier: EPL-1.0
 */

package org.eclipse.hono.service.http;

import java.net.HttpURLConnection;

import org.eclipse.hono.client.ServiceInvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.HttpStatusException;


/**
 * A generic handler for {@link ServiceInvocationException}s that have caused
 * an HTTP request to fail.
 * <p>
 * This handler can be registered on a {@code io.vertx.ext.web.Route} using its
 * <em>failureHandler</em> method. The handler inspects the route's <em>failure</em>
 * property and sets the HTTP response's status code and body based on the exception
 * type. If the error is a {@code ServiceInvocationException} then the response's
 * status code and body are set to the exception's <em>errorCode</em> and <em>message</em>
 * property values respectively. Otherwise the status code is set to 500.
 */
public class DefaultFailureHandler implements Handler<RoutingContext> {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultFailureHandler.class);

    /**
     * Handles routing failures.
     * 
     * @param ctx The failing routing context.
     */
    @Override
    public void handle(final RoutingContext ctx) {

        if (ctx.failed()) {
            LOG.debug("handling failed route for request [method: {}, URI: {}]",
                    ctx.request().method(), ctx.request().absoluteURI());
            if (ctx.response().ended()) {
                LOG.debug("cannot handle error, response already ended");
            } else if (ctx.failure() != null) {
                if (ctx.failure() instanceof ServiceInvocationException) {
                    final ServiceInvocationException e = (ServiceInvocationException) ctx.failure();
                    sendError(ctx.response(), e.getErrorCode(), e.getMessage());
                } else if (ctx.failure() instanceof HttpStatusException) {
                    final HttpStatusException e = (HttpStatusException) ctx.failure();
                    sendError(ctx.response(), e.getStatusCode(), e.getMessage());
                } else {
                    sendError(ctx.response(), HttpURLConnection.HTTP_INTERNAL_ERROR, ctx.failure().getMessage());
                }
            } else if (ctx.statusCode() != -1) {
                sendError(ctx.response(), ctx.statusCode(), ctx.response().getStatusMessage());
            } else {
                sendError(ctx.response(), HttpURLConnection.HTTP_INTERNAL_ERROR, "Internal Server Error");
            }
        } else {
            LOG.debug("skipping processing of non-failed route");
            ctx.next();
        }
    }

    private void sendError(final HttpServerResponse response, final int errorCode, final String errorMessage) {
        if (response.ended()) {
            throw new IllegalStateException("response already ended");
        } else {
            response.putHeader(HttpHeaders.CONTENT_TYPE, HttpUtils.CONTENT_TYPE_TEXT_UFT8);
            response.setStatusCode(errorCode);
            response.end(errorMessage);
        }
    }
}
