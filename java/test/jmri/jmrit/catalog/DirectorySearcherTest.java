package jmri.jmrit.catalog;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Paul Bender Copyright (C) 2017	
 */
public class DirectorySearcherTest {

    @Test
    public void testInstance() {
        DirectorySearcher t = DirectorySearcher.instance();
        Assert.assertNotNull("exists",t);
        t.close();
    }

    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }

    // private final static Logger log = LoggerFactory.getLogger(DirectorySearcherTest.class);

}
