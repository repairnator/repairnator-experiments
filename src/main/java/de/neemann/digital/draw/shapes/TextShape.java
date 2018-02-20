package de.neemann.digital.draw.shapes;

import de.neemann.digital.core.Observer;
import de.neemann.digital.core.element.ElementAttributes;
import de.neemann.digital.core.element.Keys;
import de.neemann.digital.core.element.PinDescriptions;
import de.neemann.digital.draw.elements.IOState;
import de.neemann.digital.draw.elements.Pins;
import de.neemann.digital.draw.graphics.Graphic;
import de.neemann.digital.draw.graphics.Orientation;
import de.neemann.digital.draw.graphics.Style;
import de.neemann.digital.draw.graphics.Vector;
import de.neemann.digital.lang.Lang;

/**
 * Simple text
 *
 * @author hneemann
 */
public class TextShape implements Shape {
    private final String text;
    private final int fontSize;

    /**
     * Create a new instance
     *
     * @param attr    attributes
     * @param inputs  the inputs
     * @param outputs the outputs
     */
    public TextShape(ElementAttributes attr, PinDescriptions inputs, PinDescriptions outputs) {
        String text = attr.get(Keys.DESCRIPTION);

        if (text.length() == 0) { // ToDo: used to be compatible with old files. Can be removed in the future
            text = attr.getLabel();
            if (text.length() > 0) {
                attr.set(Keys.DESCRIPTION, text);
                attr.set(Keys.LABEL, "");
            }
        }

        if (text.length() == 0)
            text = Lang.get("elem_Text");
        this.text = text;

        fontSize = attr.get(Keys.FONT_SIZE);
    }

    @Override
    public Pins getPins() {
        return new Pins();
    }

    @Override
    public Interactor applyStateMonitor(IOState ioState, Observer guiObserver) {
        return null;
    }

    @Override
    public void drawTo(Graphic graphic, Style highLight) {
        StringBuilder sb = new StringBuilder();
        Style style = Style.NORMAL.deriveFontStyle(fontSize, true);
        Vector pos = new Vector(0, 0);
        final int dy = (style.getFontSize() * 20) / 16;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                if (sb.length() > 0) {
                    graphic.drawText(pos, pos.add(1, 0), sb.toString(), Orientation.LEFTTOP, style);
                    sb.setLength(0);
                }
                pos = pos.add(0, dy);
            } else
                sb.append(c);
        }
        if (sb.length() > 0)
            graphic.drawText(pos, pos.add(1, 0), sb.toString(), Orientation.LEFTTOP, style);
    }
}
