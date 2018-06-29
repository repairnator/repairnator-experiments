/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.jersey.server.mvc.internal;

import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.WriterInterceptor;

import javax.inject.Singleton;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.model.ModelProcessor;
import org.glassfish.jersey.server.mvc.spi.ViewableContext;

/**
 * Provides MVC functionality.
 *
 * @author Michal Gajdos
 */
public class MvcBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(TemplateMethodInterceptor.class).to(WriterInterceptor.class).in(Singleton.class);
        //noinspection unchecked
        bind(ViewableMessageBodyWriter.class).to(MessageBodyWriter.class).in(Singleton.class);

        bind(TemplateModelProcessor.class).to(ModelProcessor.class).in(Singleton.class);
        bindAsContract(ResolvingViewableContext.class).in(Singleton.class);
        bind(ResolvingViewableContext.class).to(ViewableContext.class).in(Singleton.class).ranked(Integer.MIN_VALUE);
    }
}
