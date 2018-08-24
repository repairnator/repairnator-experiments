package jmri.jmrit.beantable;

import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import jmri.InstanceManager;
import jmri.Logix;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JCheckBoxOperator;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;


/*
* Tests for the LogixTableAction Class
* Re-created using JUnit4 with support for the new conditional editors
* @author Dave Sand Copyright (C) 2017
 */
public class LogixTableActionTest extends AbstractTableActionBase {

    @Test
    public void testCtor() {
        Assert.assertNotNull("LogixTableActionTest Constructor Return", new LogixTableAction());  // NOI18N
    }

    @Test
    public void testStringCtor() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        Assert.assertNotNull("LogixTableActionTest Constructor Return", new LogixTableAction("test"));  // NOI18N
    }

    @Override
    public String getTableFrameName() {
        return Bundle.getMessage("TitleLogixTable");  // NOI18N
    }

    @Override
    @Test
    public void testGetClassDescription() {
        Assert.assertEquals("Logix Table Action class description", Bundle.getMessage("TitleLogixTable"), a.getClassDescription());  // NOI18N
    }

    /**
     * Check the return value of includeAddButton.
     * <p>
     * The table generated by this action includes an Add Button.
     */
    @Override
    @Test
    public void testIncludeAddButton() {
        Assert.assertTrue("Default include add button", a.includeAddButton());  // NOI18N
    }

    @Test
    public void testLogixBrowser() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        LogixTableAction lgxTable = (LogixTableAction) a;

        lgxTable.browserPressed("IX101");  // NOI18N

        JFrame frame = JFrameOperator.waitJFrame(Bundle.getMessage("BrowserTitle"), true, true);  // NOI18N
        Assert.assertNotNull(frame);
        JUnitUtil.dispose(frame);
    }

    @Test
    public void testWhereused() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        LogixTableAction lgxTable = (LogixTableAction) a;

        lgxTable.makeWhereUsedWindow();

        JFrame frame = JFrameOperator.waitJFrame(Bundle.getMessage("DisplayWhereUsed"), true, true);  // NOI18N
        Assert.assertNotNull(frame);
        JUnitUtil.dispose(frame);
    }

    @Test
    public void testListEditor() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        LogixTableAction lgxTable = (LogixTableAction) a;

        lgxTable.editPressed("IX101");  // NOI18N
        JFrameOperator frame = new JFrameOperator(Bundle.getMessage("TitleEditLogix"));  // NOI18N
        Assert.assertNotNull(frame);
        new JButtonOperator(frame, Bundle.getMessage("ButtonDone")).push();  // NOI18N
    }

    @Test
    public void testTreeEditor() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        InstanceManager.getDefault(jmri.UserPreferencesManager.class).
                setProperty("jmri.jmrit.beantable.LogixTableAction", "Edit Mode", "TREEEDIT");  // NOI18N
        a.actionPerformed(null);
        LogixTableAction lgxTable = (LogixTableAction) a;
        JFrameOperator lgxFrame = new JFrameOperator(Bundle.getMessage("TitleLogixTable"));  // NOI18N
        Assert.assertNotNull(lgxFrame);

        lgxTable.editPressed("IX104");  // NOI18N
        JFrameOperator cdlFrame = new JFrameOperator(Bundle.getMessage("TitleEditLogix"));  // NOI18N
        Assert.assertNotNull(cdlFrame);
        new JButtonOperator(cdlFrame, Bundle.getMessage("ButtonDone")).push();  // NOI18N
        lgxFrame.dispose();
    }

    @Test
    public void testAddLogixAutoName() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        LogixTableAction lgxTable = (LogixTableAction) a;

        lgxTable.actionPerformed(null); // show table
        JFrame lgxFrame = JFrameOperator.waitJFrame(Bundle.getMessage("TitleLogixTable"), true, true);  // NOI18N
        Assert.assertNotNull("Found Logix Frame", lgxFrame);  // NOI18N

        lgxTable.addPressed(null);
        JFrameOperator addFrame = new JFrameOperator(Bundle.getMessage("TitleAddLogix"));  // NOI18N
        Assert.assertNotNull("Found Add Logix Frame", addFrame);  // NOI18N

        new JCheckBoxOperator(addFrame, 0).clickMouse();
        new JTextFieldOperator(addFrame, 1).setText("Logix 999");  // NOI18N
        new JButtonOperator(addFrame, Bundle.getMessage("ButtonCreate")).push();  // NOI18N

        Logix chk999 = jmri.InstanceManager.getDefault(jmri.LogixManager.class).getLogix("Logix 999");  // NOI18N
        Assert.assertNotNull("Verify IX999 Added", chk999);  // NOI18N

        // Add creates an edit frame; find and dispose
        JFrame editFrame = JFrameOperator.waitJFrame(Bundle.getMessage("TitleEditLogix"), true, true);  // NOI18N
        JUnitUtil.dispose(editFrame);

        JUnitUtil.dispose(lgxFrame);
    }

    @Test
    public void testAddLogix() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        LogixTableAction lgxTable = (LogixTableAction) a;

        lgxTable.actionPerformed(null); // show table
        JFrame lgxFrame = JFrameOperator.waitJFrame(Bundle.getMessage("TitleLogixTable"), true, true);  // NOI18N
        Assert.assertNotNull("Found Logix Frame", lgxFrame);  // NOI18N

        lgxTable.addPressed(null);
        JFrameOperator addFrame = new JFrameOperator(Bundle.getMessage("TitleAddLogix"));  // NOI18N
        Assert.assertNotNull("Found Add Logix Frame", addFrame);  // NOI18N

        new JTextFieldOperator(addFrame, 0).setText("105");  // NOI18N
        new JTextFieldOperator(addFrame, 1).setText("Logix 105");  // NOI18N
        new JButtonOperator(addFrame, Bundle.getMessage("ButtonCreate")).push();  // NOI18N

        Logix chk105 = jmri.InstanceManager.getDefault(jmri.LogixManager.class).getLogix("Logix 105");  // NOI18N
        Assert.assertNotNull("Verify IX105 Added", chk105);  // NOI18N

        // Add creates an edit frame; find and dispose
        JFrame editFrame = JFrameOperator.waitJFrame(Bundle.getMessage("TitleEditLogix"), true, true);  // NOI18N
        JUnitUtil.dispose(editFrame);

        JUnitUtil.dispose(lgxFrame);
    }

    @Test
    public void testDeleteLogix() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        LogixTableAction lgxTable = (LogixTableAction) a;

        lgxTable.actionPerformed(null); // show table
        JFrame lgxFrame = JFrameOperator.waitJFrame(Bundle.getMessage("TitleLogixTable"), true, true);  // NOI18N
        Assert.assertNotNull("Found Logix Frame", lgxFrame);  // NOI18N

        // Delete IX102, respond No
        createModalDialogOperatorThread(Bundle.getMessage("QuestionTitle"), Bundle.getMessage("ButtonNo"));  // NOI18N
        lgxTable.deletePressed("IX102");  // NOI18N
        Logix chk102 = jmri.InstanceManager.getDefault(jmri.LogixManager.class).getBySystemName("IX102");  // NOI18N
        Assert.assertNotNull("Verify IX102 Not Deleted", chk102);  // NOI18N

        // Delete IX103, respond Yes
        createModalDialogOperatorThread(Bundle.getMessage("QuestionTitle"), Bundle.getMessage("ButtonYes"));  // NOI18N
        lgxTable.deletePressed("IX103");  // NOI18N
        Logix chk103 = jmri.InstanceManager.getDefault(jmri.LogixManager.class).getBySystemName("IX103");  // NOI18N
        Assert.assertNull("Verify IX103 Is Deleted", chk103);  // NOI18N

        JUnitUtil.dispose(lgxFrame);
    }

    void createModalDialogOperatorThread(String dialogTitle, String buttonText) {
        Thread t = new Thread(() -> {
            // constructor for jdo will wait until the dialog is visible
            JDialogOperator jdo = new JDialogOperator(dialogTitle);
            JButtonOperator jbo = new JButtonOperator(jdo, buttonText);
            jbo.pushNoBlock();
        });
        t.setName(dialogTitle + " Close Dialog Thread");
        t.start();
    }

    @Before
    @Override
    public void setUp() {
        JUnitUtil.setUp();
        jmri.util.JUnitUtil.resetProfileManager();
        jmri.util.JUnitUtil.initLogixManager();
        jmri.util.JUnitUtil.initDefaultUserMessagePreferences();

        InstanceManager.getDefault(jmri.LogixManager.class).createNewLogix("IX101", "Logix 101");
        InstanceManager.getDefault(jmri.LogixManager.class).createNewLogix("IX102", "Logix 102");
        InstanceManager.getDefault(jmri.LogixManager.class).createNewLogix("IX103", "Logix 103");
        InstanceManager.getDefault(jmri.LogixManager.class).createNewLogix("IX104", "Logix 104");

        helpTarget = "package.jmri.jmrit.beantable.LogixTable"; 
        a = new LogixTableAction();
    }

    @After
    @Override
    public void tearDown() {
        a = null;
        JUnitUtil.tearDown();
    }
}
