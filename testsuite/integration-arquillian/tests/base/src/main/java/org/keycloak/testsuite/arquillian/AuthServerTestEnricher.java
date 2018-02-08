/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.keycloak.testsuite.arquillian;

import org.jboss.arquillian.container.spi.ContainerRegistry;
import org.jboss.arquillian.container.spi.event.StartContainer;
import org.jboss.arquillian.container.spi.event.StartSuiteContainers;
import org.jboss.arquillian.container.spi.event.StopContainer;
import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.test.spi.annotation.ClassScoped;
import org.jboss.arquillian.test.spi.annotation.SuiteScoped;
import org.jboss.arquillian.test.spi.event.suite.AfterClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;
import org.jboss.logging.Logger;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.testsuite.client.KeycloakTestingClient;
import org.keycloak.testsuite.util.LogChecker;
import org.keycloak.testsuite.util.OAuthClient;
import org.wildfly.extras.creaper.core.ManagementClient;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.OnlineOptions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import java.util.stream.Collectors;
import javax.ws.rs.NotFoundException;

/**
 *
 * @author tkyjovsk
 * @author vramik
 */
public class AuthServerTestEnricher {

    protected static final Logger log = Logger.getLogger(AuthServerTestEnricher.class);

    @Inject
    private Instance<ContainerRegistry> containerRegistry;

    @Inject
    private Event<StartContainer> startContainerEvent;
    @Inject
    private Event<StopContainer> stopContainerEvent;

    private static final String AUTH_SERVER_CONTAINER_DEFAULT = "auth-server-undertow";
    private static final String AUTH_SERVER_CONTAINER_PROPERTY = "auth.server.container";
    public static final String AUTH_SERVER_CONTAINER = System.getProperty(AUTH_SERVER_CONTAINER_PROPERTY, AUTH_SERVER_CONTAINER_DEFAULT);

    private static final String AUTH_SERVER_BACKEND_DEFAULT = AUTH_SERVER_CONTAINER + "-backend";
    private static final String AUTH_SERVER_BACKEND_PROPERTY = "auth.server.backend";
    public static final String AUTH_SERVER_BACKEND = System.getProperty(AUTH_SERVER_BACKEND_PROPERTY, AUTH_SERVER_BACKEND_DEFAULT);

    private static final String AUTH_SERVER_BALANCER_DEFAULT = "auth-server-balancer";
    private static final String AUTH_SERVER_BALANCER_PROPERTY = "auth.server.balancer";
    public static final String AUTH_SERVER_BALANCER = System.getProperty(AUTH_SERVER_BALANCER_PROPERTY, AUTH_SERVER_BALANCER_DEFAULT);

    private static final String AUTH_SERVER_CLUSTER_PROPERTY = "auth.server.cluster";
    public static final boolean AUTH_SERVER_CLUSTER = Boolean.parseBoolean(System.getProperty(AUTH_SERVER_CLUSTER_PROPERTY, "false"));
    private static final String AUTH_SERVER_CROSS_DC_PROPERTY = "auth.server.crossdc";
    public static final boolean AUTH_SERVER_CROSS_DC = Boolean.parseBoolean(System.getProperty(AUTH_SERVER_CROSS_DC_PROPERTY, "false"));

    private static final Boolean START_MIGRATION_CONTAINER = "auto".equals(System.getProperty("migration.mode")) || 
            "manual".equals(System.getProperty("migration.mode"));

    // In manual mode are all containers despite loadbalancers started in mode "manual" and nothing is managed through "suite".
    // Useful for tests, which require restart servers etc.
    private static final String MANUAL_MODE = "manual.mode";

    @Inject
    @SuiteScoped
    private InstanceProducer<SuiteContext> suiteContextProducer;
    private SuiteContext suiteContext;

    @Inject
    @ClassScoped
    private InstanceProducer<TestContext> testContextProducer;

    @Inject
    @ClassScoped
    private InstanceProducer<OAuthClient> oAuthClientProducer;

    public static String getAuthServerContextRoot() {
        return getAuthServerContextRoot(0);
    }

    public static String getAuthServerContextRoot(int clusterPortOffset) {
        String host = System.getProperty("auth.server.host", "localhost");
        int httpPort = Integer.parseInt(System.getProperty("auth.server.http.port")); // property must be set
        int httpsPort = Integer.parseInt(System.getProperty("auth.server.https.port")); // property must be set

        boolean sslRequired = Boolean.parseBoolean(System.getProperty("auth.server.ssl.required"));
        String scheme = sslRequired ? "https" : "http";
        int port = sslRequired ? httpsPort : httpPort;

        return String.format("%s://%s:%s", scheme, host, port + clusterPortOffset);
    }

    public static OnlineManagementClient getManagementClient() {
        OnlineManagementClient managementClient;
        try {
            managementClient = ManagementClient.online(OnlineOptions
                    .standalone()
                    .hostAndPort(System.getProperty("auth.server.host", "localhost"), Integer.parseInt(System.getProperty("auth.server.management.port", "10090")))
                    .build()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return managementClient;
    }
    
    public void distinguishContainersInConsoleOutput(@Observes(precedence = 5) StartContainer event) {
        log.info("*****************************************************************"
                + "*****************************************************************************");
    }

    public void initializeSuiteContext(@Observes(precedence = 2) BeforeSuite event) {
        Set<ContainerInfo> containers = containerRegistry.get().getContainers().stream()
          .map(ContainerInfo::new)
          .collect(Collectors.toSet());

        // A way to specify that containers should be in mode "manual" rather then "suite"
        checkManualMode(containers);

        suiteContext = new SuiteContext(containers);

        if (AUTH_SERVER_CROSS_DC) {
            // if cross-dc mode enabled, load-balancer is the frontend of datacenter cluster
            containers.stream()
              .filter(c -> c.getQualifier().startsWith(AUTH_SERVER_BALANCER + "-cross-dc"))
              .forEach(c -> {
                String portOffsetString = c.getArquillianContainer().getContainerConfiguration().getContainerProperties().getOrDefault("bindHttpPortOffset", "0");
                String dcString = c.getArquillianContainer().getContainerConfiguration().getContainerProperties().getOrDefault("dataCenter", "0");
                updateWithAuthServerInfo(c, Integer.valueOf(portOffsetString));
                suiteContext.addAuthServerInfo(Integer.valueOf(dcString), c);
              });

            if (suiteContext.getDcAuthServerInfo().isEmpty()) {
                throw new IllegalStateException("Not found frontend container (load balancer): " + AUTH_SERVER_BALANCER);
            }
            if (suiteContext.getDcAuthServerInfo().stream().anyMatch(Objects::isNull)) {
                throw new IllegalStateException("Frontend container (load balancer) misconfiguration");
            }

            containers.stream()
                    .filter(c -> c.getQualifier().startsWith("auth-server-" + System.getProperty("node.name") + "-"))
                    .sorted((a, b) -> a.getQualifier().compareTo(b.getQualifier()))
                    .forEach(c -> {
                        String portOffsetString = c.getArquillianContainer().getContainerConfiguration().getContainerProperties().getOrDefault("bindHttpPortOffset", "0");
                        updateWithAuthServerInfo(c, Integer.valueOf(portOffsetString));

                        String dcString = c.getArquillianContainer().getContainerConfiguration().getContainerProperties().getOrDefault("dataCenter", "0");
                        suiteContext.addAuthServerBackendsInfo(Integer.valueOf(dcString), c);
                    });
            
            containers.stream()
                    .filter(c -> c.getQualifier().startsWith("cache-server-cross-dc-"))
                    .sorted((a, b) -> a.getQualifier().compareTo(b.getQualifier()))
                    .forEach(containerInfo -> {
                        int prefixSize = "cache-server-cross-dc-".length();
                        int dcIndex = Integer.parseInt(containerInfo.getQualifier().substring(prefixSize)) -1;
                        suiteContext.addCacheServerInfo(dcIndex, containerInfo);
                    });

            if (suiteContext.getDcAuthServerInfo().isEmpty()) {
                throw new RuntimeException(String.format("No auth server container matching '%s' found in arquillian.xml.", AUTH_SERVER_BACKEND));
            }
            if (suiteContext.getDcAuthServerBackendsInfo().stream().anyMatch(Objects::isNull)) {
                throw new IllegalStateException("Frontend container (load balancer) misconfiguration");
            }
            if (suiteContext.getDcAuthServerBackendsInfo().stream().anyMatch(List::isEmpty)) {
                throw new RuntimeException(String.format("Some data center has no auth server container matching '%s' defined in arquillian.xml.", AUTH_SERVER_BACKEND));
            }
            boolean cacheServerLifecycleSkip = Boolean.parseBoolean(System.getProperty("cache.server.lifecycle.skip"));
            if (suiteContext.getCacheServersInfo().isEmpty() && !cacheServerLifecycleSkip) {
                throw new IllegalStateException("Cache containers misconfiguration");
            }

            log.info("Using frontend containers: " + this.suiteContext.getDcAuthServerInfo().stream()
              .map(ContainerInfo::getQualifier)
              .collect(Collectors.joining(", ")));
        } else if (AUTH_SERVER_CLUSTER) {
            // if cluster mode enabled, load-balancer is the frontend
            ContainerInfo container = containers.stream()
              .filter(c -> c.getQualifier().startsWith(AUTH_SERVER_BALANCER))
              .findAny()
              .orElseThrow(() -> new IllegalStateException("Not found frontend container: " + AUTH_SERVER_BALANCER));
            updateWithAuthServerInfo(container);
            suiteContext.setAuthServerInfo(container);

            containers.stream()
                .filter(c -> c.getQualifier().startsWith(AUTH_SERVER_BACKEND))
                .sorted((a, b) -> a.getQualifier().compareTo(b.getQualifier())) // ordering is expected by the cluster tests
                .forEach(c -> {
                    int portOffset = Integer.parseInt(c.getQualifier().substring(AUTH_SERVER_BACKEND.length()));
                    updateWithAuthServerInfo(c, portOffset);
                    suiteContext.addAuthServerBackendsInfo(0, c);
                });

            if (suiteContext.getAuthServerBackendsInfo().isEmpty()) {
                throw new RuntimeException(String.format("No auth server container matching '%s' found in arquillian.xml.", AUTH_SERVER_BACKEND));
            }

            log.info("Using frontend container: " + container.getQualifier());
        } else {
            // frontend-only
            ContainerInfo container = containers.stream()
              .filter(c -> c.getQualifier().startsWith(AUTH_SERVER_CONTAINER))
              .findAny()
              .orElseThrow(() -> new IllegalStateException("Not found frontend container: " + AUTH_SERVER_CONTAINER));
            updateWithAuthServerInfo(container);
            suiteContext.setAuthServerInfo(container);
        }

        if (START_MIGRATION_CONTAINER) {
            // init migratedAuthServerInfo
            for (ContainerInfo container : suiteContext.getContainers()) {
                // migrated auth server
                if (container.getQualifier().equals("auth-server-jboss-migration")) {
                    updateWithAuthServerInfo(container);
                    suiteContext.setMigratedAuthServerInfo(container);
                }
            }
            // validate setup
            if (suiteContext.getMigratedAuthServerInfo() == null) {
                throw new RuntimeException(String.format("Migration test was enabled but no auth server from which to migrate was activated. "
                        + "A container matching auth-server-jboss-migration needs to be enabled in arquillian.xml."));
            }
        }

        suiteContextProducer.set(suiteContext);
        log.info("\n\n" + suiteContext);
    }

    private ContainerInfo updateWithAuthServerInfo(ContainerInfo authServerInfo) {
        return updateWithAuthServerInfo(authServerInfo, 0);
    }

    private ContainerInfo updateWithAuthServerInfo(ContainerInfo authServerInfo, int clusterPortOffset) {
        try {
            authServerInfo.setContextRoot(new URL(getAuthServerContextRoot(clusterPortOffset)));
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException(ex);
        }
        return authServerInfo;
    }

    public void startMigratedContainer(@Observes(precedence = 3) StartSuiteContainers event) {
        if (suiteContext.isAuthServerMigrationEnabled()) {
            log.info("\n\n### Starting keycloak " + System.getProperty("migrated.auth.server.version", "- previous") + " ###\n\n");
            startContainerEvent.fire(new StartContainer(suiteContext.getMigratedAuthServerInfo().getArquillianContainer()));
        }
    }

    public void runPreMigrationTask(@Observes(precedence = 2) StartSuiteContainers event) throws Exception {
        if (suiteContext.isAuthServerMigrationEnabled()) {
            log.info("\n\n### Run preMigration task on keycloak " + System.getProperty("migrated.auth.server.version", "- previous") + " ###\n\n");
            suiteContext.getMigrationContext().runPreMigrationTask();
        }
    }

    public void stopMigratedContainer(@Observes(precedence = 1) StartSuiteContainers event) {
        if (suiteContext.isAuthServerMigrationEnabled()) {
            log.info("## STOP old container: " + suiteContext.getMigratedAuthServerInfo().getQualifier());
            stopContainerEvent.fire(new StopContainer(suiteContext.getMigratedAuthServerInfo().getArquillianContainer()));
        }
    }

    public void checkServerLogs(@Observes(precedence = -1) BeforeSuite event) throws IOException, InterruptedException {
        boolean checkLog = Boolean.parseBoolean(System.getProperty("auth.server.log.check", "true"));
        if (checkLog && suiteContext.getAuthServerInfo().isJBossBased()) {
            String jbossHomePath = suiteContext.getAuthServerInfo().getProperties().get("jbossHome");
            LogChecker.checkJBossServerLog(jbossHomePath);
        }
    }

    public void initializeTestContext(@Observes(precedence = 2) BeforeClass event) {
        TestContext testContext = new TestContext(suiteContext, event.getTestClass().getJavaClass());
        testContextProducer.set(testContext);
    }

    public void initializeOAuthClient(@Observes(precedence = 3) BeforeClass event) {
        // TODO workaround. Check if can be removed
        OAuthClient.updateURLs(suiteContext.getAuthServerInfo().getContextRoot().toString());
        OAuthClient oAuthClient = new OAuthClient();
        oAuthClientProducer.set(oAuthClient);
    }

    public void afterClass(@Observes(precedence = 2) AfterClass event) {
        TestContext testContext = testContextProducer.get();

        Keycloak adminClient = testContext.getAdminClient();
        KeycloakTestingClient testingClient = testContext.getTestingClient();

        removeTestRealms(testContext, adminClient);

        if (adminClient != null) {
            adminClient.close();
        }

        if (testingClient != null) {
            testingClient.close();
        }
    }


    public static void removeTestRealms(TestContext testContext, Keycloak adminClient) {
        List<RealmRepresentation> testRealmReps = testContext.getTestRealmReps();
        if (testRealmReps != null) {
            log.info("removing test realms after test class");
            for (RealmRepresentation testRealm : testRealmReps) {
                String realmName = testRealm.getRealm();
                log.info("removing realm: " + realmName);
                try {
                    adminClient.realms().realm(realmName).remove();
                } catch (NotFoundException e) {
                    // Ignore
                }
            }
        }
    }


    private void checkManualMode(Set<ContainerInfo> containers) {
        String manualMode = System.getProperty(MANUAL_MODE);

        if (Boolean.parseBoolean(manualMode)) {

            containers.stream()
                    .filter(containerInfo -> !containerInfo.getQualifier().contains("balancer"))
                    .forEach(containerInfo -> {
                        log.infof("Container '%s' will be in manual mode", containerInfo.getQualifier());
                        containerInfo.getArquillianContainer().getContainerConfiguration().setMode("manual");
                    });
        }
    }

}
