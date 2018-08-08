package jmri.jmrit.operations.rollingstock.cars.tools;

import java.awt.GraphicsEnvironment;
import jmri.jmrit.operations.rollingstock.cars.tools.CarAttributeEditFrame;
import jmri.jmrit.operations.rollingstock.cars.tools.CarDeleteAttributeAction;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Paul Bender Copyright (C) 2017	
 */
public class CarDeleteAttributeActionTest {

    @Test
    public void testCTor() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        CarAttributeEditFrame f = new CarAttributeEditFrame();
        CarDeleteAttributeAction t = new CarDeleteAttributeAction("Test Action",f);
        Assert.assertNotNull("exists",t);
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

    // private final static Logger log = LoggerFactory.getLogger(CarDeleteAttributeActionTest.class);

}
