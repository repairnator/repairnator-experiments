/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License (the "License").
 * You may not use this file except in compliance with the License.
 *
 * See LICENSE.txt included in this distribution for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at LICENSE.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 */

 /*
 * Copyright (c) 2017, Chris Fraire <cfraire@me.com>.
 */
package org.opensolaris.opengrok.condition;

/**
 * Represents an intentional exception thrown for testing purposes
 * <p>
 * https://stackoverflow.com/a/1754473/933163
 * https://stackoverflow.com/questions/1754315/how-to-create-custom-exceptions-in-java
 */
public class DeliberateRuntimeException extends RuntimeException {
      public DeliberateRuntimeException() { super(); }
      public DeliberateRuntimeException(String message) { super(message); }
      public DeliberateRuntimeException(String message, Throwable cause) { super(message, cause); }
      public DeliberateRuntimeException(Throwable cause) { super(cause); }
}
