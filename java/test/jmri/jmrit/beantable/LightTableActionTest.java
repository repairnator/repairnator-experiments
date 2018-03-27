package jmri.jmrit.beantable;

import apps.gui.GuiLafPreferencesManager;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import jmri.InstanceManager;
import jmri.Light;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.jemmy.operators.JFrameOperator;

/**
 * Tests for the jmri.jmrit.beantable.LightTableAction class.
 * @author Paul Bender Copyright (C) 2017
 */
public class LightTableActionTest extends AbstractTableActionBase {

    @Test
    public void testCTor() {
        Assert.assertNotNull("exists",a);
    }

    @Override
    public String getTableFrameName(){
        return Bundle.getMessage("TitleLightTable");
    }

    @Override
    @Test
    public void testGetClassDescription(){
         Assert.assertEquals("Light Table Action class description","Light Table",a.getClassDescription());
    }

    /**
     * Check the return value of includeAddButton.  The table generated by
     * this action includes an Add Button.
     */
    @Override
    @Test
    public void testIncludeAddButton(){
         Assert.assertTrue("Default include add button",a.includeAddButton());
    }

    /**
     * Check graphic state presentation.
     * @since 4.7.4
     */
    @Test
    public void testAddAndInvoke() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());

        a.actionPerformed(null); // show table
        // create 2 lights and see if they exist
        Light il1 = InstanceManager.lightManagerInstance().provideLight("IL1");
        InstanceManager.lightManagerInstance().provideLight("IL2");
        il1.setState(Light.ON);
        il1.setState(Light.OFF);

        // set graphic state column display preference to false, read by createModel()
        InstanceManager.getDefault(GuiLafPreferencesManager.class).setGraphicTableState(false);

        LightTableAction _lTable;
        _lTable = new LightTableAction();
        Assert.assertNotNull("found LightTable frame", _lTable);

        // set to true, use icons
        InstanceManager.getDefault(GuiLafPreferencesManager.class).setGraphicTableState(true);
        LightTableAction _l1Table;
        _l1Table = new LightTableAction();
        Assert.assertNotNull("found LightTable1 frame", _l1Table);

        _l1Table.addPressed(null);
        JFrame af = JFrameOperator.waitJFrame(Bundle.getMessage("TitleAddLight"), true, true);
        Assert.assertNotNull("found Add frame", af);

//        // wait 1 sec (nothing to see)
//        Runnable waiter = new Runnable() {
//            @Override
//            public synchronized void run() {
//                try {
//                    this.wait(1000);
//                } catch (InterruptedException ex) {
//                    log.error("Waiter interrupted.");
//                }
//            }
//        };
//        waiter.run();

        // close AddPane
        _l1Table.cancelPressed(null);
        // TODO Add more Light Add pane tests in new LightTableWindowTest? see TurnoutEtc

        // clean up
        JFrame f = a.getFrame();
        if (f != null) {
            JUnitUtil.dispose(f);
        }
        JUnitUtil.dispose(af);
        _lTable.dispose();
        _l1Table.dispose();
    }

    // The minimal setup for log4J
    @Before
    @Override
    public void setUp() {
        apps.tests.Log4JFixture.setUp();
        jmri.util.JUnitUtil.initInternalLightManager();
        jmri.util.JUnitUtil.resetInstanceManager();
        jmri.util.JUnitUtil.initDefaultUserMessagePreferences();
        a = new LightTableAction();
    }

    @After
    @Override
    public void tearDown() {
        a = null;
        jmri.util.JUnitUtil.resetInstanceManager();
        jmri.util.JUnitUtil.initDefaultUserMessagePreferences();
        apps.tests.Log4JFixture.tearDown();
    }

    // private final static Logger log = LoggerFactory.getLogger(LightTableActionTest.class);

}
