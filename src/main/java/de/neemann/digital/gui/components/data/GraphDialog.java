package de.neemann.digital.gui.components.data;

import de.neemann.digital.core.*;
import de.neemann.digital.core.element.ElementAttributes;
import de.neemann.digital.data.ValueTable;
import de.neemann.digital.draw.graphics.*;
import de.neemann.digital.gui.SaveAsHelper;
import de.neemann.digital.gui.Settings;
import de.neemann.digital.gui.components.OrderMerger;
import de.neemann.digital.gui.components.testing.ValueTableDialog;
import de.neemann.digital.gui.sync.NoSync;
import de.neemann.digital.gui.sync.Sync;
import de.neemann.digital.lang.Lang;
import de.neemann.gui.IconCreator;
import de.neemann.gui.MyFileChooser;
import de.neemann.gui.ToolTipAction;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The Dialog which shows the data to plot.
 *
 * @author hneemann
 */
public class GraphDialog extends JDialog implements Observer {
    private static final int MAX_SAMPLE_SIZE = 1000;
    private final GraphComponent dsc;
    private final JScrollPane scrollPane;
    private final ToolTipAction showTable;

    private static final Icon ICON_EXPAND = IconCreator.create("View-zoom-fit.png");
    private static final Icon ICON_ZOOM_IN = IconCreator.create("View-zoom-in.png");
    private static final Icon ICON_ZOOM_OUT = IconCreator.create("View-zoom-out.png");

    /**
     * Creates a instance prepared for "live logging"
     *
     * @param owner     the parent frame
     * @param model     the model
     * @param microStep stepping mode
     * @param ordering  the ordering to use
     * @param modelSync the lock to access the model
     * @return the created instance
     */
    public static GraphDialog createLiveDialog(JFrame owner, Model model, boolean microStep, List<String> ordering, Sync modelSync) {
        String title;
        if (microStep)
            title = Lang.get("win_measures_microstep");
        else
            title = Lang.get("win_measures_fullstep");

        ArrayList<Signal> signals = model.getSignalsCopy();
        new OrderMerger<String, Signal>(ordering) {
            @Override
            public boolean equals(Signal a, String b) {
                return a.getName().equals(b);
            }
        }.order(signals);

        ValueTableObserver valueTableObserver = new ValueTableObserver(microStep, signals, MAX_SAMPLE_SIZE);

        GraphDialog graphDialog = new GraphDialog(owner, title, valueTableObserver.getLogData(), modelSync);

        graphDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                modelSync.access(() -> model.addObserver(valueTableObserver));
            }

            @Override
            public void windowClosed(WindowEvent e) {
                modelSync.access(() -> model.removeObserver(valueTableObserver));
            }
        });

        return graphDialog;
    }

    /**
     * Creates a new instance
     *
     * @param owner   the parent frame
     * @param title   the frame title
     * @param logData the data to visualize
     */
    public GraphDialog(Window owner, String title, ValueTable logData) {
        this(owner, title, logData, NoSync.INST);
    }

    /**
     * Creates a new instance
     *
     * @param owner     the parent frame
     * @param title     the frame title
     * @param logData   the data to visualize
     * @param modelSync used to access the running model
     */
    private GraphDialog(Window owner, String title, ValueTable logData, Sync modelSync) {
        super(owner, title, ModalityType.MODELESS);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        dsc = new GraphComponent(logData, modelSync);
        scrollPane = new JScrollPane(dsc);
        getContentPane().add(scrollPane);
        dsc.setScrollPane(scrollPane);

        logData.addObserver(this);

        JToolBar toolBar = new JToolBar();
        ToolTipAction maximize = new ToolTipAction(Lang.get("menu_maximize"), ICON_EXPAND) {
            @Override
            public void actionPerformed(ActionEvent e) {
                dsc.fitData(scrollPane.getWidth() - scrollPane.getInsets().left - scrollPane.getInsets().right);
            }
        }.setAccelerator("F1");
        ToolTipAction zoomIn = new ToolTipAction(Lang.get("menu_zoomIn"), ICON_ZOOM_IN) {
            @Override
            public void actionPerformed(ActionEvent e) {
                Rectangle r = scrollPane.getViewport().getViewRect();
                dsc.scale(1.25f, r.x + r.width / 2);
            }
        }.setAccelerator("control PLUS");
        ToolTipAction zoomOut = new ToolTipAction(Lang.get("menu_zoomOut"), ICON_ZOOM_OUT) {
            @Override
            public void actionPerformed(ActionEvent e) {
                Rectangle r = scrollPane.getViewport().getViewRect();
                dsc.scale(0.8f, r.x + r.width / 2);
            }
        }.setAccelerator("control MINUS");

        showTable = new ToolTipAction(Lang.get("menu_showDataAsTable")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ValueTableDialog(GraphDialog.this, title) // ToDo pass modelSync to ValueTableDialog
                        .addValueTable(Lang.get("win_data"), logData)
                        .disableGraph()
                        .setVisible(true);
            }
        }.setToolTip(Lang.get("menu_showDataAsTable_tt"));

        toolBar.add(zoomIn.createJButtonNoText());
        toolBar.add(zoomOut.createJButtonNoText());
        toolBar.add(maximize.createJButtonNoText());

        getContentPane().add(toolBar, BorderLayout.NORTH);
        pack();

        scrollPane.getViewport().setPreferredSize(dsc.getPreferredSize());

        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu(Lang.get("menu_file"));
        bar.add(file);
        file.add(new ToolTipAction(Lang.get("menu_saveData")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new MyFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Comma Separated Values", "csv"));
                new SaveAsHelper(GraphDialog.this, fileChooser, "csv")
                        .checkOverwrite(logData::saveCSV);
            }
        }.setToolTip(Lang.get("menu_saveData_tt")).createJMenuItem());
        file.add(new ExportAction(Lang.get("menu_exportSVG"), GraphicSVGIndex::new).createJMenuItem());
        file.add(new ExportAction(Lang.get("menu_exportSVGLaTex"), GraphicSVGLaTeX::new).createJMenuItem());

        JMenu view = new JMenu(Lang.get("menu_view"));
        bar.add(view);
        view.add(maximize.createJMenuItem());
        view.add(zoomOut.createJMenuItem());
        view.add(zoomIn.createJMenuItem());
        view.addSeparator();
        view.add(showTable.createJMenuItem());

        setJMenuBar(bar);
        pack();
        setLocationRelativeTo(owner);
    }

    private final AtomicBoolean paintPending = new AtomicBoolean();

    @Override
    public void hasChanged() {
        if (paintPending.compareAndSet(false, true)) {
            SwingUtilities.invokeLater(() -> {
                dsc.revalidate();
                dsc.repaint();
                SwingUtilities.invokeLater(() -> {
                    JScrollBar bar = scrollPane.getHorizontalScrollBar();
                    bar.setValue(bar.getMaximum());
                    paintPending.set(false);
                });
            });
        }
    }

    /**
     * Disable the show as table function
     *
     * @return this for chained calls
     */
    public GraphDialog disableTable() {
        showTable.setEnabled(false);
        return this;
    }

    private final class ExportAction extends ToolTipAction {
        private final ExportFactory factory;

        private ExportAction(String title, ExportFactory factory) {
            super(title);
            this.factory = factory;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new MyFileChooser();

            ElementAttributes settings = Settings.getInstance().getAttributes();
            File exportDir = settings.getFile("exportDirectory");
            if (exportDir != null)
                fileChooser.setCurrentDirectory(exportDir);


            fileChooser.setFileFilter(new FileNameExtensionFilter("SVG", "svg"));
            new SaveAsHelper(GraphDialog.this, fileChooser, "svg")
                    .checkOverwrite(file -> {
                        settings.setFile("exportDirectory", file.getParentFile());
                        try (Graphic gr = factory.create(new FileOutputStream(file))) {
                            GraphicMinMax minMax = new GraphicMinMax();
                            dsc.getPlotter().drawTo(minMax, null);
                            gr.setBoundingBox(minMax.getMin(), minMax.getMax());
                            dsc.getPlotter().drawTo(gr, null);
                        }
                    });
        }

    }
}
