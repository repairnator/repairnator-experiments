/* 
 * Copyright 2017 Javier A. Ortiz Bultron javier.ortiz.78@gmail.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.validation.manager.core.server;

import com.validation.manager.core.VMException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.JNDIConnector;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.server.ServerSession;

/**
 * See http://wiki.eclipse.org/Customizing_the_EclipseLink_Application_(ELUG)
 * Use for clients that would like to use a JTA SE pu instead of a
 * RESOURCE_LOCAL SE pu.
 */
public class JPAEclipseLinkSessionCustomizer implements SessionCustomizer {

    public static final String JNDI_DATASOURCE_NAME = "java:comp/env/jdbc/VMDB";
    private static final Logger LOG
            = getLogger(JPAEclipseLinkSessionCustomizer.class.getSimpleName());

    /**
     * Get a dataSource connection and set it on the session with
     * lookupType=STRING_LOOKUP
     *
     * @param session Session
     * @throws java.lang.Exception If an error occurs.
     */
    @Override
    public void customize(Session session) throws Exception {
        JNDIConnector connector;
        // Initialize session customizer
        DataSource dataSource;
        try {
            Context context = new InitialContext();
            if (null == context) {
                throw new VMException("Context is null");
            }
            connector = (JNDIConnector) session.getLogin().getConnector(); // possible CCE
            // Lookup this new dataSource
            dataSource = (DataSource) context.lookup(JNDI_DATASOURCE_NAME);
            connector.setDataSource(dataSource);
            // Change from COMPOSITE_NAME_LOOKUP to STRING_LOOKUP
            // Note: if both jta and non-jta elements exist this will only change the first one - and may still result in the COMPOSITE_NAME_LOOKUP being set
            // Make sure only jta-data-source is in persistence.xml with no non-jta-data-source property set
            connector.setLookupType(JNDIConnector.STRING_LOOKUP);

            // if you are specifying both JTA and non-JTA in your persistence.xml then set both connectors to be safe
            JNDIConnector writeConnector
                    = (JNDIConnector) session.getLogin().getConnector();
            writeConnector.setLookupType(JNDIConnector.STRING_LOOKUP);
            JNDIConnector readConnector
                    = (JNDIConnector) ((DatabaseLogin) ((ServerSession) session)
                            .getReadConnectionPool().getLogin()).getConnector();
            readConnector.setLookupType(JNDIConnector.STRING_LOOKUP);

            // Set the new connection on the session
            session.getLogin().setConnector(connector);
        }
        catch (Exception e) {
            LOG.log(Level.SEVERE, JNDI_DATASOURCE_NAME, e);
        }
    }
}
