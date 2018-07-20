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
package org.eclipse.ditto.signals.commands.connectivity.query;

import org.eclipse.ditto.model.base.headers.DittoHeaders;
import org.eclipse.ditto.signals.commands.base.WithEntity;

import org.eclipse.ditto.signals.commands.connectivity.ConnectivityCommandResponse;

/**
 * Aggregates all {@link ConnectivityCommandResponse} which respond to a
 * {@link ConnectivityQueryCommand}.
 *
 * @param <T> the type of the implementing class.
 */
public interface ConnectivityQueryCommandResponse<T extends ConnectivityQueryCommandResponse> extends
        ConnectivityCommandResponse<T>, WithEntity {

    @Override
    T setDittoHeaders(DittoHeaders dittoHeaders);

}
