package org.jboss.resteasy.test.client;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.category.NotForForwardCompatibility;
import org.jboss.resteasy.utils.TestUtil;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * @tpSubChapter Resteasy-client
 * @tpChapter Unit tests
 * @tpSince RESTEasy 3.0.17
 */
@RunWith(Arquillian.class)
public class ClientBuilderTest {

    @SuppressWarnings(value = "unchecked")
    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(ClientBuilderTest.class.getSimpleName());
        war.addClass(TestUtil.class);
        war.addClass(NotForForwardCompatibility.class);
        return TestUtil.finishContainerPrepare(war, null, (Class<?>[]) null);
    }


    public static class FeatureReturningFalse implements Feature {
        @Override
        public boolean configure(FeatureContext context) {
            // false returning feature is not to be registered
            return false;
        }
    }

    private int getWarningCount() {
        return TestUtil.getWarningCount("RESTEASY002155", true);
    }

    /**
     * @tpTestDetails Register class twice to the client
     * @tpPassCrit Warning will be raised that second class registration is ignored
     * @tpSince RESTEasy 3.0.17
     */
    @Test
    public void testDoubleClassRegistration() {
        int initCount = getWarningCount();
        Client client = ClientBuilder.newClient();
        int count = client.getConfiguration().getClasses().size();
        client.register(FeatureReturningFalse.class).register(FeatureReturningFalse.class);

        Assert.assertEquals("RESTEASY002155 log not found", 1, getWarningCount() - initCount);
        Assert.assertEquals(count + 1, client.getConfiguration().getClasses().size());
        client.close();
    }

    /**
     * @tpTestDetails Register provider instance twice to the client
     * @tpPassCrit Warning will be raised that second provider instance registration is ignored
     * @tpSince RESTEasy 3.0.17
     */
    @Test
    @Category({NotForForwardCompatibility.class})
    public void testDoubleRegistration() {
        int countRESTEASY002160 = TestUtil.getWarningCount("RESTEASY002160", true);
        int countRESTEASY002155 = TestUtil.getWarningCount("RESTEASY002155", true);
        Client client = ClientBuilder.newClient();
        int count = client.getConfiguration().getInstances().size();
        Object reg = new FeatureReturningFalse();

        client.register(reg).register(reg);
        client.register(FeatureReturningFalse.class).register(FeatureReturningFalse.class);
        Assert.assertEquals("Expect 1 warnining messages of Provider instance is already registered", 1, TestUtil.getWarningCount("RESTEASY002160", true) - countRESTEASY002160);
        Assert.assertEquals("Expect 1 warnining messages of Provider class is already registered", 2, TestUtil.getWarningCount("RESTEASY002155", true) - countRESTEASY002155);
        Assert.assertEquals(count + 1, client.getConfiguration().getInstances().size());

        client.close();
    }
}
