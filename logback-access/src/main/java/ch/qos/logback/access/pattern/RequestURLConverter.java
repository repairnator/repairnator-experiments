/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2015, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package ch.qos.logback.access.pattern;

import ch.qos.logback.access.spi.IAccessEvent;

/**
 * The first line of the request.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class RequestURLConverter extends AccessConverter {

    @Override
    public String convert(IAccessEvent accessEvent) {
        return accessEvent.getRequestURL();
    }
}
