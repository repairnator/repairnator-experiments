/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.javaee7.websocket.endpoint.programmatic;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.PongMessage;
import javax.websocket.Session;

/**
 * @author Arun Gupta
 */
public class MyEndpoint extends Endpoint {

    @Override
    public void onOpen(final Session session, EndpointConfig ec) {
        session.addMessageHandler(new MessageHandler.Whole<String>() {

            @Override
            public void onMessage(String text) {
                try {
                    MyEndpointTextClient.latch.countDown();
                    session.getBasicRemote().sendText(text);
                } catch (IOException ex) {
                    Logger.getLogger(MyEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {

            @Override
            public void onMessage(ByteBuffer t) {
                try {
                    MyEndpointBinaryClient.latch.countDown();
                    session.getBasicRemote().sendBinary(t);
                } catch (IOException ex) {
                    Logger.getLogger(MyEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        session.addMessageHandler(new MessageHandler.Whole<PongMessage>() {

            @Override
            public void onMessage(PongMessage t) {
                System.out.println("PongMessage received: " + t.getApplicationData());
            }
        });

    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        System.err.println("Closing: " + closeReason.getReasonPhrase());
    }

    @Override
    public void onError(Session session, Throwable t) {
        System.err.println("Error: " + t.getLocalizedMessage());
    }
}
