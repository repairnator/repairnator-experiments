package jmri.jmrit.display.palette;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import jmri.jmrit.display.DisplayFrame;
import jmri.jmrit.display.Editor;
import jmri.jmrit.display.Positionable;
import jmri.jmrit.display.PositionableLabel;
import jmri.jmrit.display.PositionablePopupUtil;
import jmri.jmrit.display.SensorIcon;
import jmri.jmrit.display.palette.TextItemPanel.DragDecoratorLabel;
import jmri.util.swing.ImagePanel;

/**
 * Panel for positionables with text and/or colored margins and borders.
 * @see ItemPanel palette class diagram
 * 
 * @author PeteCressman Copyright (C) 2009, 2015
 */
public class DecoratorPanel extends JPanel implements ChangeListener, ItemListener {

    static final String[] JUSTIFICATION = {Bundle.getMessage("left"),
        Bundle.getMessage("center"),
        Bundle.getMessage("right")};

    static final String[] STYLES = {Bundle.getMessage("Plain"),
        Bundle.getMessage("Bold"),
        Bundle.getMessage("Italic"),
        Bundle.getMessage("Bold/italic")};

    static final String[] FONTSIZE = {"6", "8", "10", "11", "12", "14", "16",
        "20", "24", "28", "32", "36"};
    public static final int SIZE = 1;
    public static final int STYLE = 2;
    public static final int JUST = 3;
    public static final int FONT = 4;

    AJComboBox _fontBox;
    AJComboBox _fontSizeBox;
    AJComboBox _fontStyleBox;
    AJComboBox _fontJustBox;

    public static final int STRUT = 6;

    public static final int BORDER = 1;
    public static final int MARGIN = 2;
    public static final int FWIDTH = 3;
    public static final int FHEIGHT = 4;

    public static final int TEXT_FONT = 10;
    public static final int ACTIVE_FONT = 11;
    public static final int INACTIVE_FONT = 12;
    public static final int UNKOWN_FONT = 13;
    public static final int INCONSISTENT_FONT = 14;
    public static final int TEXT_BACKGROUND = 20;
    public static final int ACTIVE_BACKGROUND = 21;
    public static final int INACTIVE_BACKGROUND = 22;
    public static final int UNKOWN_BACKGROUND = 23;
    public static final int INCONSISTENT_BACKGROUND = 24;
    public static final int TRANSPARENT_COLOR = 30;
    public static final int ACTIVE_TRANSPARENT_COLOR = 31;
    public static final int INACTIVE_TRANSPARENT_COLOR = 32;
    public static final int UNKNOWN_TRANSPARENT_COLOR = 33;
    public static final int INCONSISTENT_TRANSPARENT_COLOR = 34;
    public static final int BORDER_COLOR = 40;

    AJSpinner _borderSpin;
    AJSpinner _marginSpin;
    AJSpinner _widthSpin;
    AJSpinner _heightSpin;

    JColorChooser _chooser;
    ImagePanel _previewPanel;
    JPanel _samplePanel;
    private PositionablePopupUtil _util;
    private Hashtable<String, PositionableLabel> _sample = null;
    private int _selectedButton;
    ButtonGroup _buttonGroup = new ButtonGroup();
    AJRadioButton _fontButton;
    AJRadioButton _borderButton;
    AJRadioButton _backgroundButton;

    protected BufferedImage[] _backgrounds; // array of Image backgrounds
    protected JComboBox<String> _bgColorBox;

    Editor _editor;
    protected DisplayFrame _paletteFrame;

    public DecoratorPanel(Editor editor, DisplayFrame paletteFrame) {
        _editor = editor;
        _paletteFrame = paletteFrame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Color panelBackground = _editor.getTargetPanel().getBackground(); // start using Panel background color
        // create array of backgrounds, _currentBackground already set and used
        _backgrounds = ItemPanel.makeBackgrounds(null,  panelBackground);
        _chooser = new JColorChooser(panelBackground);
        _sample = new Hashtable<>();

        _previewPanel = new ImagePanel();
        _previewPanel.setLayout(new BorderLayout());
        _previewPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1),
                Bundle.getMessage("PreviewBorderTitle")));
        _previewPanel.add(Box.createVerticalStrut(STRUT), BorderLayout.PAGE_START);
        _previewPanel.add(Box.createVerticalStrut(STRUT), BorderLayout.PAGE_END);

        _samplePanel = new JPanel();
        _samplePanel.add(Box.createHorizontalStrut(STRUT));
        _samplePanel.setOpaque(false);
    }

    static class AJComboBox extends JComboBox/*<Class<?>> - can't get this to work*/ {
        int _which;

        AJComboBox(Font[] items, int which) {
            super(items);
            _which = which;
        }
        AJComboBox(String[] items, int which) {
            super(items);
            _which = which;
        }
    }

    private JPanel makeBoxPanel(String caption, JComboBox<Class<?>> box) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel(Bundle.getMessage(caption)));
        box.addItemListener(this);
        panel.add(box);
        return panel;
    }

    static class AJSpinner extends JSpinner {
        int _which;

        AJSpinner(SpinnerModel model, int which) {
            super(model);
            _which = which;
        }
    }

    static class AJRadioButton extends JRadioButton {
        int which;

        AJRadioButton(String text, int w) {
            super(text);
            which = w;
        }
    }

    private JPanel makeSpinPanel(String caption, JSpinner spin) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel(Bundle.getMessage(caption)));
        spin.addChangeListener(this);
        panel.add(spin);
        return panel;
    }

    /* Called by Palette's TextItemPanel i.e. make a new panel item to drag */
    protected void initDecoratorPanel(DragDecoratorLabel sample) {
        sample.setDisplayLevel(Editor.LABELS);
        sample.setBackground(_editor.getTargetPanel().getBackground());
        _util = sample.getPopupUtility();
        _sample.put("Text", sample);
        makeFontPanels();
        this.add(makeTextPanel("Text", sample, TEXT_FONT, true));
        _samplePanel.add(sample);
        finishInit(true);
    }

    /* Called by Editor's TextAttrDialog - i.e. update a panel item from menu */
    public void initDecoratorPanel(Positionable pos) {
        Positionable item = pos.deepClone(); // need copy of PositionableJPanel in PopupUtility
        _util = item.getPopupUtility();
        item.remove();      // don't need copy any more. Removes ghost image of PositionableJPanels
        makeFontPanels();

        if (pos instanceof SensorIcon && !((SensorIcon)pos).isIcon()) {
            SensorIcon si = (SensorIcon) pos;
            if (!si.isIcon() && si.isText()) {
                PositionableLabel sample = new PositionableLabel(si.getActiveText(), _editor);
                sample.setForeground(si.getTextActive());
                Color color = si.getBackgroundActive();
                if (color!=null) {
                    sample.setBackground(color);
                    sample.setOpaque(true);
                }
                doPopupUtility("Active", ACTIVE_FONT, sample, true); // NOI18N

                sample = new PositionableLabel(si.getInactiveText(), _editor);
                sample.setForeground(si.getTextInActive());
                color = si.getBackgroundInActive();
                if (color!=null) {
                    sample.setBackground(color);
                    sample.setOpaque(true);
                }
                doPopupUtility("InActive", INACTIVE_FONT, sample, true); // NOI18N

                sample = new PositionableLabel(si.getUnknownText(), _editor);
                sample.setForeground(si.getTextUnknown());
                color = si.getBackgroundUnknown();
                if (color!=null) {
                    sample.setBackground(color);
                    sample.setOpaque(true);
                }
                doPopupUtility("Unknown", UNKOWN_FONT, sample, true); // NOI18N

                sample = new PositionableLabel(si.getInconsistentText(), _editor);
                sample.setForeground(si.getTextInconsistent());
                color = si.getBackgroundInconsistent();
                if (color!=null) {
                    sample.setBackground(color);
                    sample.setOpaque(true);
                }
                doPopupUtility("Inconsistent", INCONSISTENT_FONT, sample, true); // NOI18N
            }
        } else { // not a SensorIcon
            PositionableLabel sample = new PositionableLabel("", _editor);
            sample.setForeground(pos.getForeground());
            sample.setBackground(pos.getBackground());
            sample.setOpaque(_util.hasBackground());
            boolean addtextField;
            if (pos instanceof PositionableLabel) {
                sample.setText(((PositionableLabel)pos).getUnRotatedText());
                if (pos instanceof jmri.jmrit.display.MemoryIcon) {
                    addtextField = false;                    
                } else {
                    addtextField = true;
                }
            } else if (pos instanceof jmri.jmrit.display.MemoryInputIcon) {
                JTextField field = (JTextField)((jmri.jmrit.display.MemoryInputIcon)pos).getTextComponent();
                sample.setText(field.getText());
                addtextField = false;
            } else if (pos instanceof jmri.jmrit.display.MemoryComboIcon) {
                JComboBox<String> box = ((jmri.jmrit.display.MemoryComboIcon)pos).getTextComponent();
                sample.setText(box.getSelectedItem().toString());
                addtextField = false;
            } else if (pos instanceof jmri.jmrit.display.MemorySpinnerIcon) {
                JTextField field = (JTextField)((jmri.jmrit.display.MemorySpinnerIcon)pos).getTextComponent();
                sample.setText(field.getText());
                addtextField = false;
            } else {
                addtextField = true;
            }
            doPopupUtility("Text", TEXT_FONT, sample, addtextField);
        }
        finishInit(false);
    }

    private void finishInit(boolean addBgCombo) {
        _chooser.getSelectionModel().addChangeListener(this);
        _chooser.setPreviewPanel(new JPanel());
        add(_chooser);
        _previewPanel.add(_samplePanel, BorderLayout.CENTER);

        // add a SetBackground combo
        if (addBgCombo) {
            add(add(makeBgButtonPanel(_previewPanel, null, _backgrounds))); // no listener on this variant            
        }
        add(_previewPanel);
        _previewPanel.setImage(_backgrounds[0]);
        _previewPanel.revalidate();        // force redraw
        updateSamples();
        setButtonSelected(_fontButton);
    }

    private void doPopupUtility(String type, int which, 
            PositionableLabel sample, boolean editText) {
        PositionablePopupUtil util = sample.getPopupUtility();
        util.setJustification(_util.getJustification());
        util.setHorizontalAlignment(_util.getJustification());
        util.setFixedWidth(_util.getFixedWidth());
        util.setFixedHeight(_util.getFixedHeight());
        util.setMargin(_util.getMargin());
        util.setBorderSize(_util.getBorderSize());
        util.setBorderColor(_util.getBorderColor());
        util.setFont(util.getFont().deriveFont(_util.getFontStyle()));
        util.setFontSize(_util.getFontSize());
        util.setFontStyle(_util.getFontStyle());
        util.setOrientation(_util.getOrientation());
        sample.updateSize();
       
        _sample.put(type, sample);
        this.add(makeTextPanel(type, sample, which, editText));
        _samplePanel.add(sample);
        _samplePanel.add(Box.createHorizontalStrut(STRUT));
    }

    protected void makeFontPanels() {
        JPanel fontPanel = new JPanel();
        
        Font defaultFont = _util.getFont();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String fontFamilyNames[] = ge.getAvailableFontFamilyNames();
        Font[] fonts = new Font[fontFamilyNames.length];
        int k = 0;
        for (String fontFamilyName : fontFamilyNames) {
            fonts[k++] = new Font(fontFamilyName, defaultFont.getStyle(), defaultFont.getSize()) {
                @Override
                public String toString() {
                    return getFamily();
                }
            };
        }
        _fontBox = new AJComboBox(fonts, FONT);
        fontPanel.add(makeBoxPanel("EditFont", _fontBox)); // NOI18N
        _fontBox.setSelectedItem(defaultFont);
        
        _fontSizeBox = new AJComboBox(FONTSIZE, SIZE);
        fontPanel.add(makeBoxPanel("FontSize", _fontSizeBox)); // NOI18N
        int row = 4;
        for (int i = 0; i < FONTSIZE.length; i++) {
            if (_util.getFontSize() == Integer.parseInt(FONTSIZE[i])) {
                row = i;
                break;
            }
        }
        _fontSizeBox.setSelectedIndex(row);

        _fontStyleBox = new AJComboBox(STYLES, STYLE);
        fontPanel.add(makeBoxPanel("FontStyle", _fontStyleBox)); // NOI18N
        _fontStyleBox.setSelectedIndex(_util.getFont().getStyle());

        _fontJustBox = new AJComboBox(JUSTIFICATION, JUST);
        fontPanel.add(makeBoxPanel("Justification", _fontJustBox)); // NOI18N
        switch (_util.getJustification()) {
            case PositionablePopupUtil.LEFT:
                row = 0;
                break;
            case PositionablePopupUtil.RIGHT:
                row = 2;
                break;
            case PositionablePopupUtil.CENTRE:
                row = 1;
                break;
            default:
                row = 2;
        }
        _fontJustBox.setSelectedIndex(row);
        this.add(fontPanel);

        JPanel sizePanel = new JPanel();
        SpinnerNumberModel model = new SpinnerNumberModel(_util.getBorderSize(), 0, 100, 1);
        _borderSpin = new AJSpinner(model, BORDER);
        sizePanel.add(makeSpinPanel("borderSize", _borderSpin));
        model = new SpinnerNumberModel(_util.getMargin(), 0, 100, 1);
        _marginSpin = new AJSpinner(model, MARGIN);
        sizePanel.add(makeSpinPanel("marginSize", _marginSpin));
        model = new SpinnerNumberModel(_util.getFixedWidth(), 0, 1000, 1);
        _widthSpin = new AJSpinner(model, FWIDTH);
        sizePanel.add(makeSpinPanel("fixedWidth", _widthSpin));
        model = new SpinnerNumberModel(_util.getFixedHeight(), 0, 1000, 1);
        _heightSpin = new AJSpinner(model, FHEIGHT);
        sizePanel.add(makeSpinPanel("fixedHeight", _heightSpin));
        this.add(sizePanel);
    }

    String bundleCaption = null;

    private JPanel makeTextPanel(String caption, JLabel sample, int state, boolean addTextField) {
        JPanel panel = new JPanel();
        // use NamedBeanBundle property for basic beans like "Turnout" I18N
        if ("Active".equals(caption)) {
            bundleCaption = "SensorStateActive";
        } else if ("InActive".equals(caption)) {
            bundleCaption = "SensorStateInactive";
        } else if ("Unknown".equals(caption)) {
            bundleCaption = "BeanStateUnknown";
        } else if ("Inconsistent".equals(caption)) {
            bundleCaption = "BeanStateInconsistent";
        } else {
            bundleCaption = caption;
        }
        panel.setBorder(BorderFactory.createTitledBorder(Bundle.getMessage(bundleCaption)));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel p = new JPanel();
        if (addTextField) {
            JTextField textField = new JTextField(sample.getText(), 25);
            textField.addKeyListener(new KeyListener() {
                JLabel sample;

                KeyListener init(JLabel s) {
                    sample = s;
                    return this;
                }

                @Override
                public void keyTyped(KeyEvent E) {
                }

                @Override
                public void keyPressed(KeyEvent E) {
                }

                @Override
                public void keyReleased(KeyEvent E) {
                    JTextField tmp = (JTextField) E.getSource();
                    sample.setText(tmp.getText());
                    updateSamples();
                }
            }.init(sample));
            p.add(textField);            
        }
        panel.add(p);

        p = new JPanel();
        _fontButton = new AJRadioButton(Bundle.getMessage("FontColor"), state);
        p.add(makeButton(_fontButton));
        
        _backgroundButton = new AJRadioButton(Bundle.getMessage("FontBackgroundColor"), state + 10);
        p.add(makeButton(_backgroundButton));
        
        AJRadioButton button = new AJRadioButton(Bundle.getMessage("transparentBack"), state + 20);
        _buttonGroup.add(button);
        p.add(button);
        button.addActionListener(new ActionListener() {
            AJRadioButton button;

            @Override
            public void actionPerformed(ActionEvent a) {
                if (button.isSelected()) {
                    switch (button.which) {
                        case TRANSPARENT_COLOR:
                            _sample.get("Text").setOpaque(false);
                            break;
                        case ACTIVE_TRANSPARENT_COLOR:
                            _sample.get("Active").setOpaque(false);
                            break;
                        case INACTIVE_TRANSPARENT_COLOR:
                            _sample.get("InActive").setOpaque(false);
                            break;
                        case UNKNOWN_TRANSPARENT_COLOR:
                            _sample.get("Unknown").setOpaque(false);
                            break;
                        case INCONSISTENT_TRANSPARENT_COLOR:
                            _sample.get("Inconsistent").setOpaque(false);
                            break;
                        default:
                            log.warn("Unexpected button.which {} in actionPerformed", button.which);
                            break;
                    }
                    updateSamples();
                }
            }

            ActionListener init(AJRadioButton b) {
                button = b;
                return this;
            }
        }.init(button));
        _borderButton = new AJRadioButton(Bundle.getMessage("borderColor"), BORDER_COLOR);
        p.add(makeButton(_borderButton));

        panel.add(p);
        return panel;
    }

    private AJRadioButton makeButton(AJRadioButton button) {
        button.addActionListener(new ActionListener() {
            AJRadioButton button;

            @Override
            public void actionPerformed(ActionEvent a) {
                if (button.isSelected()) {
                    int prevButton = _selectedButton;
                    _selectedButton = button.which;
                    if (Math.abs(prevButton-_selectedButton)<5) {
                        changeColor();                        
                    }
                }
            }

            ActionListener init(AJRadioButton b) {
                button = b;
                return this;
            }
        }.init(button));
        _buttonGroup.add(button);            
        return button;
    }

    protected void updateSamples() {
        if (_previewPanel == null) {
            return;            
        }
        
        int mar = _util.getMargin();
        int bor = _util.getBorderSize();
        Border outlineBorder;
        if (bor == 0) {
            outlineBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
        } else {
            outlineBorder = new LineBorder(_util.getBorderColor(), bor);
        }
        Font font = _util.getFont();
        int just = _util.getJustification();
        
        Iterator<PositionableLabel> it = _sample.values().iterator();
        while (it.hasNext()) {
            PositionableLabel sam = it.next();
            PositionablePopupUtil util = sam.getPopupUtility();
            sam.setFont(font);
            util.setFixedWidth(_util.getFixedWidth());
            util.setFixedHeight(_util.getFixedHeight());
            util.setMargin(mar);
            util.setBorderSize(bor);
            Border borderMargin;
            if (sam.isOpaque()) {
                borderMargin = new LineBorder(sam.getBackground(), mar);
            } else {
                borderMargin = BorderFactory.createEmptyBorder(mar, mar, mar, mar);
            }
            sam.setBorder(new CompoundBorder(outlineBorder, borderMargin));
            
            switch (just) {
                case PositionablePopupUtil.LEFT:
                    sam.setHorizontalAlignment(JLabel.LEFT);
                    break;
                case PositionablePopupUtil.RIGHT:
                    sam.setHorizontalAlignment(JLabel.RIGHT);
                    break;
                default:
                    sam.setHorizontalAlignment(JLabel.CENTER);
            }
            sam.updateSize();
            sam.setPreferredSize(sam.getSize());
            sam.repaint();
        }
    }

    /**
     * Create panel element containing [Set background:] drop down list.
     * Special version for Decorator, no access to shared variable previewBgSet.
     * @see jmri.jmrit.catalog.PreviewDialog#setupPanel()
     * @see ItemPanel
     *
     * @param preview1 ImagePanel containing icon set
     * @param preview2 not used, matches method in ItemPanel
     * @param imgArray array of colored background images
     * @return a JPanel with label and drop down
     */
    private JPanel makeBgButtonPanel(@Nonnull ImagePanel preview1, ImagePanel preview2, BufferedImage[] imgArray) {
        _bgColorBox = new JComboBox<>();
        _bgColorBox.addItem(Bundle.getMessage("PanelBgColor")); // PanelColor key is specific for CPE, too long for combo
        _bgColorBox.addItem(Bundle.getMessage("White"));
        _bgColorBox.addItem(Bundle.getMessage("LightGray"));
        _bgColorBox.addItem(Bundle.getMessage("DarkGray"));
        _bgColorBox.addItem(Bundle.getMessage("Checkers"));
        int index;
        if (_paletteFrame != null) {
            index = _paletteFrame.getPreviewBg();
        } else {
            index = 0;
        }
        _bgColorBox.setSelectedIndex(index);
        _bgColorBox.addActionListener((ActionEvent e) -> {
            if (imgArray != null) {
                // index may repeat
                int previewBgSet = _bgColorBox.getSelectedIndex(); // store user choice
                if (_paletteFrame != null) {
                    _paletteFrame.setPreviewBg(previewBgSet);
                }
                // load background image
                log.debug("Palette Decorator setImage called {}", previewBgSet);
                preview1.setImage(imgArray[previewBgSet]);
                // preview.setOpaque(false); // needed?
                preview1.revalidate();        // force redraw
            } else {
                log.debug("imgArray is empty");
            }
        });
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        JPanel pp = new JPanel();
        pp.setLayout(new FlowLayout(FlowLayout.CENTER));
        pp.add(new JLabel(Bundle.getMessage("setBackground")));
        pp.add(_bgColorBox);
        backgroundPanel.add(pp);
        backgroundPanel.setMaximumSize(backgroundPanel.getPreferredSize());
        return backgroundPanel;
    }

    // called when editor changed
    protected BufferedImage[] getBackgrounds() {
        return _backgrounds;
    }
    // called when editor changed
    protected void setBackgrounds(BufferedImage[] imgArray) {
        _backgrounds = imgArray;
        _previewPanel.setImage(imgArray[0]);
        _previewPanel.revalidate();        // force redraw
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object obj = e.getSource();
        if (obj instanceof AJSpinner) {
            int num = ((Number) ((AJSpinner) obj).getValue()).intValue();
            switch (((AJSpinner) obj)._which) {
                case BORDER:
                    _util.setBorderSize(num);
                    setButtonSelected(_borderButton);
                    break;
                case MARGIN:
                    _util.setMargin(num);
                    setButtonSelected(_backgroundButton);
                    break;
                case FWIDTH:
                    _util.setFixedWidth(num);
                    setButtonSelected(_backgroundButton);
                    break;
                case FHEIGHT:
                    _util.setFixedHeight(num);
                    setButtonSelected(_backgroundButton);
                    break;
                default:
                    log.warn("Unexpected _which {}  in stateChanged", ((AJSpinner) obj)._which);
                    break;
            }
        } else {
            changeColor();
        }
        updateSamples();
    }

    public PositionablePopupUtil getPositionablePopupUtil() {
        return _util;
    }

    public void setAttributes(Positionable pos) {
        if (pos instanceof SensorIcon  && !((SensorIcon)pos).isIcon()) {
            SensorIcon icon = (SensorIcon) pos;
            PositionableLabel sample = _sample.get("Active");
            if (sample.isOpaque()) {
                icon.setBackgroundActive(sample.getBackground());                
            } else {
                icon.setBackgroundActive(null);                
            }
            icon.setTextActive(sample.getForeground());
            icon.setActiveText(sample.getText());

            sample = _sample.get("InActive");
            icon.setInactiveText(sample.getText());
            if (sample.isOpaque()) {
                icon.setBackgroundInActive(sample.getBackground());                
            } else {
                icon.setBackgroundInActive(null);                
            }
            icon.setTextInActive(sample.getForeground());

            sample = _sample.get("Unknown");
            icon.setUnknownText(sample.getText());
            if (sample.isOpaque()) {
                icon.setBackgroundUnknown(sample.getBackground());                
            } else {
                icon.setBackgroundUnknown(null);                
            }
            icon.setTextUnknown(sample.getForeground());

            sample = _sample.get("Inconsistent");
            icon.setInconsistentText(sample.getText());
            if (sample.isOpaque()) {
                icon.setBackgroundInconsistent(sample.getBackground());                
            } else {
                icon.setBackgroundInconsistent(null);                
            }
            icon.setTextInconsistent(sample.getForeground());
        } else {
            PositionableLabel sample = _sample.get("Text");
            pos.setForeground(sample.getForeground());
            if ( pos instanceof PositionableLabel &&
                !(pos instanceof jmri.jmrit.display.MemoryIcon)) {
                ((PositionableLabel) pos).setText(sample.getText());
            }
            PositionablePopupUtil util = pos.getPopupUtility();
            if (sample.isOpaque()) {
                util.setBackgroundColor(sample.getBackground());                
            } else {
                util.setBackgroundColor(null);                
            }
            util.setHasBackground(sample.isOpaque());
            util.setFont(_util.getFont());
        }
    }
    
    private void changeColor() {
        switch (_selectedButton) {
            case TEXT_FONT:
                _sample.get("Text").setForeground(_chooser.getColor());
                _util.setForeground(_chooser.getColor());
                break;
            case ACTIVE_FONT:
                _sample.get("Active").setForeground(_chooser.getColor());
                break;
            case INACTIVE_FONT:
                _sample.get("InActive").setForeground(_chooser.getColor());
                break;
            case UNKOWN_FONT:
                _sample.get("Unknown").setForeground(_chooser.getColor());
                break;
            case INCONSISTENT_FONT:
                _sample.get("Inconsistent").setForeground(_chooser.getColor());
                break;
            case TEXT_BACKGROUND:
                _sample.get("Text").setBackground(_chooser.getColor());
                _sample.get("Text").setOpaque(true);
                _util.setBackgroundColor(_chooser.getColor());
                break;
            case ACTIVE_BACKGROUND:
                _sample.get("Active").setBackground(_chooser.getColor());
                _sample.get("Active").setOpaque(true);
                break;
            case INACTIVE_BACKGROUND:
                _sample.get("InActive").setBackground(_chooser.getColor());
                _sample.get("InActive").setOpaque(true);
                break;
            case UNKOWN_BACKGROUND:
                _sample.get("Unknown").setBackground(_chooser.getColor());
                _sample.get("Unknown").setOpaque(true);
                break;
            case INCONSISTENT_BACKGROUND:
                _sample.get("Inconsistent").setBackground(_chooser.getColor());
                _sample.get("Inconsistent").setOpaque(true);
                break;
            case BORDER_COLOR:
                _util.setBorderColor(_chooser.getColor());
                break;
            default:
                log.warn("Unexpected _selectedButton {}  in changeColor", _selectedButton);
                break;
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        AJComboBox comboBox = (AJComboBox)e.getSource();
        switch (comboBox._which) {
            case SIZE:
                String size = (String) comboBox.getSelectedItem();
                _util.setFontSize(Float.valueOf(size));
                setButtonSelected(_fontButton);
                break;
            case STYLE:
                int style = 0;
                switch (comboBox.getSelectedIndex()) {
                    case 0:
                        style = Font.PLAIN;
                        break;
                    case 1:
                        style = Font.BOLD;
                        break;
                    case 2:
                        style = Font.ITALIC;
                        break;
                    case 3:
                        style = (Font.BOLD | Font.ITALIC);
                        break;
                    default:
                        log.warn("Unexpected index {}  in itemStateChanged", comboBox.getSelectedIndex());
                        break;
                }
                _util.setFontStyle(style);
                setButtonSelected(_fontButton);
                break;
            case JUST:
                int just = 0;
                switch (comboBox.getSelectedIndex()) {
                    case 0:
                        just = PositionablePopupUtil.LEFT;
                        break;
                    case 1:
                        just = PositionablePopupUtil.CENTRE;
                        break;
                    case 2:
                        just = PositionablePopupUtil.RIGHT;
                        break;
                    default:
                        log.warn("Unexpected index {}  in itemStateChanged", comboBox.getSelectedIndex());
                        break;
                }
                _util.setJustification(just);
                break;
            case FONT:
                Font font = (Font) comboBox.getSelectedItem();
                _util.setFont(font);
                setButtonSelected(_fontButton);
                break;
            default:
                log.warn("Unexpected _which {}  in itemStateChanged", comboBox._which);
                break;
            }
        updateSamples();
    }

    private void setButtonSelected(AJRadioButton button) {
        if (button != null) {
            _selectedButton = button.which;
            button.setSelected(true);
        }
    }

    // initialize logging
    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DecoratorPanel.class);

}
