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
 * Copyright (c) 2016, 2017, Oracle and/or its affiliates. All rights reserved.
 * Portions Copyright (c) 2017, Chris Fraire <cfraire@me.com>.
 */
package org.opensolaris.opengrok.authorization;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.opensolaris.opengrok.condition.DeliberateRuntimeException;
import org.opensolaris.opengrok.configuration.Group;
import org.opensolaris.opengrok.configuration.Nameable;
import org.opensolaris.opengrok.configuration.Project;
import org.opensolaris.opengrok.web.DummyHttpServletRequest;

@RunWith(Parameterized.class)
public class AuthorizationFrameworkTest {

    private static final Random random = new Random();

    private AuthorizationFramework framework;
    private final StackSetup setup;

    public AuthorizationFrameworkTest(StackSetup setup) {
        this.setup = setup;
    }

    @Parameterized.Parameters
    public static StackSetup[][] params() {
        return new StackSetup[][]{
            // -------------------------------------------------------------- //
            //
            // Test no plugins setup. This should always return true if there
            // are no plugins loaded or configured.
            //
            // -------------------------------------------------------------- //
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED),
                // no plugins => true
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()),
                NewTest(true, createUnallowedProject()),
                NewTest(true, createUnallowedGroup()))
            },
            // -------------------------------------------------------------- //
            //
            // Test authorization flags for plugins. Both plugins do not fail
            // during the operation.
            //
            // -------------------------------------------------------------- //
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createNotAllowedPrefixPlugin()),
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin())
                ),
                // sufficient returns true => true
                NewTest(true, createUnallowedProject()),
                NewTest(true, createUnallowedGroup()),
                // sufficient return false
                // required returns true => true
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            }, //
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createAllowedPrefixPlugin()),
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createNotAllowedPrefixPlugin())
                ),
                // sufficient return false
                // required returns true => true
                NewTest(true, createUnallowedProject()),
                NewTest(true, createUnallowedGroup()),
                // sufficient returns true => true
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createNotAllowedPrefixPlugin()),
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createAllowedPrefixPlugin())
                ),
                // all plugins are sufficient => true
                NewTest(true, createUnallowedProject()),
                NewTest(true, createUnallowedGroup()),
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createNotAllowedPrefixPlugin()),
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createAllowedPrefixPlugin())
                ),
                // required returns true
                // the rest is sufficient => true
                NewTest(true, createUnallowedProject()),
                NewTest(true, createUnallowedGroup()),
                // required returns false => false
                // the rest is sufficient => false
                NewTest(false, createAllowedProject()),
                NewTest(false, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createAllowedPrefixPlugin()),
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin())
                ),
                // sufficient return false
                // required returns false => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                // sufficient returns true => true
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin()),
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin())
                ),
                // required returns false
                // required returns false => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                // required returns true
                // required returns true => true
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin()),
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createNotAllowedPrefixPlugin())
                ),
                // requisite returns false
                // the rest is sufficient => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                // requisite returns true
                // the rest is sufficient => true
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUISITE, createAllowedPrefixPlugin()),
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createNotAllowedPrefixPlugin())
                ),
                // requisite return true
                // required returns false => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                NewTest(false, createAllowedProject()),
                NewTest(false, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUISITE, createNotAllowedPrefixPlugin()),
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin())
                ),
                // requisite returns true
                // requisite returns false => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                // requisite returns false
                // requisite returns true => false
                NewTest(false, createAllowedProject()),
                NewTest(false, createAllowedGroup()))
            },
            // -------------------------------------------------------------- //
            //
            // Test authorization flags for plugins. One of the plugin fails
            // during the load operation and is marked as failed for all of the
            // operation (returning false always).
            //
            // -------------------------------------------------------------- //
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createLoadFailingPlugin()),
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin())
                ),
                // sufficient return false
                // required returns false => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                // sufficient return false
                // required returns true => true
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createLoadFailingPlugin()),
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createAllowedPrefixPlugin())
                ),
                // all are sufficient => true
                NewTest(true, createUnallowedProject()),
                NewTest(true, createUnallowedGroup()),
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createLoadFailingPlugin()),
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createAllowedPrefixPlugin())
                ),
                // required returns false
                // the rest is sufficient => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                NewTest(false, createAllowedProject()),
                NewTest(false, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createLoadFailingPlugin()),
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createLoadFailingPlugin())
                ),
                // sufficient returns false
                // required returns false => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                NewTest(false, createAllowedProject()),
                NewTest(false, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createLoadFailingPlugin()),
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createLoadFailingPlugin())
                ),
                // required returns false
                // the rest is sufficient => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                NewTest(false, createAllowedProject()),
                NewTest(false, createAllowedGroup()))
            },
            // -------------------------------------------------------------- //
            //
            // Test authorization flags for plugins. One of the plugin fails
            // during the test operation and the result of that decision for
            // this particular plugin is false.
            //
            //
            // -------------------------------------------------------------- //
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createTestFailingPlugin()),
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin())
                ),
                // sufficient return false
                // required returns false => false
                NewTest(false, createUnallowedProject()),
                // sufficient return false
                // required returns false => false
                NewTest(false, createUnallowedGroup()),
                // sufficient return false
                // required returns true => true
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createTestFailingPlugin()),
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createAllowedPrefixPlugin())
                ),
                // all are sufficient => true
                NewTest(true, createUnallowedProject()),
                NewTest(true, createUnallowedGroup()),
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createTestFailingPlugin()),
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createAllowedPrefixPlugin())
                ),
                // required returns false
                // the rest is sufficient => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                NewTest(false, createAllowedProject()),
                NewTest(false, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createTestFailingPlugin()),
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createTestFailingPlugin())
                ),
                // sufficient returns false
                // required returns false => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                NewTest(false, createAllowedProject()),
                NewTest(false, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createTestFailingPlugin()),
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createTestFailingPlugin())
                ),
                // required returns false => false
                // the rest is sufficient => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                NewTest(false, createAllowedProject()),
                NewTest(false, createAllowedGroup()))
            },
            // -------------------------------------------------------------- //
            //
            // Test authorization flags for plugins in multiple stacks.
            //
            // -------------------------------------------------------------- //
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                NewStack(AuthControlFlag.SUFFICIENT,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin())),
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin()))
                ),
                // sufficient stack returns false
                // required stack returns false => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                // sufficient stack returns true => true
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                NewStack(AuthControlFlag.SUFFICIENT,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin())),
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createNotAllowedPrefixPlugin()))
                ),
                // sufficient stack returns false
                // required stack returns true => true
                NewTest(true, createUnallowedProject()),
                NewTest(true, createUnallowedGroup()),
                // sufficient stack returns true => true
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin())),
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createNotAllowedPrefixPlugin()))
                ),
                // required stack1 returns false
                // required stack2 returns true => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                // required stack1 returns true
                // required stack2 returns false => false
                NewTest(false, createAllowedProject()),
                NewTest(false, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                NewStack(AuthControlFlag.SUFFICIENT,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin())),
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createNotAllowedPrefixPlugin()))
                ),
                // sufficient stack returns false
                // required stack returns true => true
                NewTest(true, createUnallowedProject()),
                NewTest(true, createUnallowedGroup()),
                // sufficient stack returns true => true
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                NewStack(AuthControlFlag.SUFFICIENT,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin())),
                NewStack(AuthControlFlag.SUFFICIENT,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createNotAllowedPrefixPlugin()))
                ),
                // all stacks are sufficient => true
                NewTest(true, createUnallowedProject()),
                NewTest(true, createUnallowedGroup()),
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createAllowedPrefixPlugin())),
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createNotAllowedPrefixPlugin()))
                ),
                // all plugins are sufficient => true
                NewTest(true, createUnallowedProject()),
                NewTest(true, createUnallowedGroup()),
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                NewStack(AuthControlFlag.REQUISITE,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin())),
                NewStack(AuthControlFlag.SUFFICIENT,
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createAllowedPrefixPlugin())),
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createNotAllowedPrefixPlugin()))
                ),
                // requisite stack returns false
                // sufficient stack return false
                // required stack returns true => false (requisite)
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                // requisite stack returns true
                // sufficient stack return true => true
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            // -------------------------------------------------------------- //
            //
            // Test authorization flags for plugins in multiple stacks. Some of
            // the plugins fail during the load operation and is marked as
            // failed for all of the operation (returning false always).
            //
            // -------------------------------------------------------------- //
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                NewStack(AuthControlFlag.SUFFICIENT,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createLoadFailingPlugin())),
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin()))
                ),
                // sufficient stack returns false
                // required stack returns false => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                // sufficient stack returns false
                // required stack returns true => true
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createLoadFailingPlugin())),
                NewStack(AuthControlFlag.SUFFICIENT,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin()))
                ),
                // required stack returns false => false
                // the rest is sufficient => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                NewTest(false, createAllowedProject()),
                NewTest(false, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createLoadFailingPlugin())),
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin()))
                ),
                // required stack returns true (sufficient plugin failed has no effect)
                // required stack returns false => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                // required stack returns true (sufficient plugin failed has no effect)
                // required stack returns true => true
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createLoadFailingPlugin())),
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createLoadFailingPlugin()))
                ),
                // required stack returns true (sufficient plugin failed has no effect)
                // required stack returns false => false
                NewTest(true, createUnallowedProject()),
                NewTest(true, createUnallowedGroup()),
                // required stack returns true (sufficient plugin failed has no effect)
                // required stack returns true => true
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createLoadFailingPlugin())),
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createLoadFailingPlugin()))
                ),
                // required stack returns false
                // required stack returns false => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                NewTest(false, createAllowedProject()),
                NewTest(false, createAllowedGroup()))
            },
            // -------------------------------------------------------------- //
            //
            // Test authorization flags for plugins. Some of the plugins fail
            // during the test operation and the result of that decision for
            // this particular plugin is false.
            //
            // -------------------------------------------------------------- //
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                NewStack(AuthControlFlag.SUFFICIENT,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createTestFailingPlugin())),
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin()))
                ),
                // sufficient stack returns false
                // required stack returns false => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                // sufficient stack returns false
                // required stack returns true => true
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createTestFailingPlugin())),
                NewStack(AuthControlFlag.SUFFICIENT,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin()))
                ),
                // required stack returns false => false
                // the rest is sufficient => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                NewTest(false, createAllowedProject()),
                NewTest(false, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createTestFailingPlugin())),
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createAllowedPrefixPlugin()))
                ),
                // required stack returns true (sufficient plugin failed has no effect)
                // required stack returns false => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                // required stack returns true (sufficient plugin failed has no effect)
                // required stack returns true => true
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createTestFailingPlugin())),
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.SUFFICIENT, createTestFailingPlugin()))
                ),
                // required stack returns true (sufficient plugin failed has no effect)
                // required stack returns false => false
                NewTest(true, createUnallowedProject()),
                NewTest(true, createUnallowedGroup()),
                // required stack returns true (sufficient plugin failed has no effect)
                // required stack returns true => true
                NewTest(true, createAllowedProject()),
                NewTest(true, createAllowedGroup()))
            },
            {
                new StackSetup(
                NewStack(AuthControlFlag.REQUIRED,
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createTestFailingPlugin())),
                NewStack(AuthControlFlag.REQUIRED,
                new AuthorizationPlugin(AuthControlFlag.REQUIRED, createTestFailingPlugin()))
                ),
                // required stack returns false
                // required stack returns false => false
                NewTest(false, createUnallowedProject()),
                NewTest(false, createUnallowedGroup()),
                NewTest(false, createAllowedProject()),
                NewTest(false, createAllowedGroup()))
            },
        };
    }

    @Test
    public void testPluginsGeneric() {
        framework.setStack(setup.stack);
        framework.loadAllPlugins(setup.stack);

        boolean actual;
        String format = "%s <%s> was <%s> for entity %s";

        for (TestCase innerSetup : setup.setup) {
            try {
                actual = framework.isAllowed(innerSetup.request, (Group) innerSetup.entity);
                Assert.assertEquals(String.format(format, setup.toString(), innerSetup.expected, actual, innerSetup.entity.getName()),
                        innerSetup.expected,
                        actual);
            } catch (ClassCastException ex) {
                actual = framework.isAllowed(innerSetup.request, (Project) innerSetup.entity);
                Assert.assertEquals(String.format(format, setup.toString(), innerSetup.expected, actual, innerSetup.entity.getName()),
                        innerSetup.expected,
                        actual);
            }
        }
    }

    @Before
    public void setUp() {
        framework = new AuthorizationFramework(null);
    }

    static private Project createAllowedProject() {
        Project p = new Project("allowed" + "_" + "project" + Math.random());
        return p;
    }

    static private Project createUnallowedProject() {
        Project p = new Project("not_allowed" + "_" + "project" + Math.random());
        return p;
    }

    static private Group createAllowedGroup() {
        Group g = new Group("allowed" + "_" + "group_" + random.nextInt());
        return g;
    }

    static private Group createUnallowedGroup() {
        Group g = new Group("not_allowed" + "_" + "group_" + random.nextInt());
        return g;
    }

    static private HttpServletRequest createRequest() {
        return new DummyHttpServletRequest();
    }

    static private IAuthorizationPlugin createAllowedPrefixPlugin() {
        return new TestPlugin() {
            @Override
            public boolean isAllowed(HttpServletRequest request, Project project) {
                return project.getName().startsWith("allowed");
            }

            @Override
            public boolean isAllowed(HttpServletRequest request, Group group) {
                return group.getName().startsWith("allowed");
            }

            @Override
            public String toString() {
                return "allowed prefix";
            }
        };
    }

    static private IAuthorizationPlugin createNotAllowedPrefixPlugin() {
        return new TestPlugin() {
            @Override
            public boolean isAllowed(HttpServletRequest request, Project project) {
                return project.getName().startsWith("not_allowed");
            }

            @Override
            public boolean isAllowed(HttpServletRequest request, Group group) {
                return group.getName().startsWith("not_allowed");
            }

            @Override
            public String toString() {
                return "not_allowed prefix";
            }
        };
    }

    static private IAuthorizationPlugin createLoadFailingPlugin() {
        return new TestPlugin() {
            @Override
            public void load(Map<String, Object> parameters) {
                throw new DeliberateRuntimeException("This plugin failed while loading.");
            }

            @Override
            public boolean isAllowed(HttpServletRequest request, Project project) {
                return true;
            }

            @Override
            public boolean isAllowed(HttpServletRequest request, Group group) {
                return true;
            }

            @Override
            public String toString() {
                return "load failing";
            }

        };
    }

    static private IAuthorizationPlugin createTestFailingPlugin() {
        return new TestPlugin() {
            @Override
            public boolean isAllowed(HttpServletRequest request, Project project) {
                throw new DeliberateRuntimeException("This plugin failed while checking.");
            }

            @Override
            public boolean isAllowed(HttpServletRequest request, Group group) {
                throw new DeliberateRuntimeException("This plugin failed while checking.");
            }

            @Override
            public String toString() {
                return "test failing";
            }

        };

    }

    static public class TestCase {

        public boolean expected;
        public HttpServletRequest request;
        public Nameable entity;

        public TestCase(boolean expected, HttpServletRequest request, Nameable entity) {
            this.expected = expected;
            this.request = request;
            this.entity = entity;
        }

        @Override
        public String toString() {
            return "expected <" + expected + "> for entity " + entity.getName();
        }
    }

    static public class StackSetup {

        public AuthorizationStack stack;
        public List<TestCase> setup;

        public StackSetup(AuthorizationStack stack, TestCase... setups) {
            this.stack = stack;
            this.setup = Arrays.asList(setups);
        }

        @Override
        public String toString() {
            return stack.getFlag().toString().toUpperCase() + "[" + printStack(stack) + "] " + "-> {\n"
                    + setup.stream().map((t) -> t.toString()).collect(Collectors.joining(",\n")) + "\n"
                    + "}";
        }

        private String printStack(AuthorizationStack s) {
            String x = new String();
            for (AuthorizationEntity entity : s.getStack()) {
                if (entity instanceof AuthorizationPlugin) {
                    x += ((AuthorizationPlugin) entity).getPlugin().toString() + ", ";
                } else {
                    x += entity.getFlag().toString().toUpperCase() + "[" + printStack((AuthorizationStack) entity) + "], ";
                }
            }
            return x.replaceAll(", $", "");
        }
    }

    static private AuthorizationStack NewStack(AuthControlFlag flag, AuthorizationEntity... entities) {
        AuthorizationStack stack = new AuthorizationStack(flag, "stack-" + entities.hashCode());
        for (AuthorizationEntity entity : entities) {
            stack.add(entity);
        }
        return stack;
    }

    static private TestCase NewTest(boolean expected, Nameable entity) {
        return NewTest(expected, createRequest(), entity);
    }

    static private TestCase NewTest(boolean expected, HttpServletRequest request, Nameable entity) {
        return new TestCase(expected, request, entity);
    }
}
