package de.neemann.digital.draw.graphics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

import static de.neemann.digital.core.element.ElementAttributes.cleanLabel;

/**
 * Used to draw on a {@link Graphics2D} instance.
 *
 * @author hneemann
 */
public class GraphicSwing implements Graphic {

    private final int minFontSize;
    private int pixelSize;
    private Style lastStyle;
    private Graphics2D gr;

    /**
     * Creates a new instance
     *
     * @param gr the {@link Graphics2D} instance to use.
     */
    public GraphicSwing(Graphics2D gr) {
        this(gr, 1);
    }

    /**
     * Creates a new instance
     *
     * @param gr        the {@link Graphics2D} instance to use.
     * @param pixelSize the size of one pixel
     */
    public GraphicSwing(Graphics2D gr, int pixelSize) {
        this.gr = gr;
        this.pixelSize = pixelSize;
        this.minFontSize = pixelSize * 3;
        if (gr != null)
            gr.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    /**
     * Set the graphics instance to use
     *
     * @param gr the Graphics2D to draw to
     */
    protected void setGraphics2D(Graphics2D gr) {
        this.gr = gr;
    }

    @Override
    public void drawLine(Vector p1, Vector p2, Style style) {
        applyStyle(style);
        gr.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    @Override
    public void drawPolygon(Polygon p, Style style) {
        applyStyle(style);
        Path2D path = new GeneralPath();
        //modification of loop variable i is intended!
        //CHECKSTYLE.OFF: ModifiedControlVariable
        for (int i = 0; i < p.size(); i++) {
            if (i == 0) {
                path.moveTo(p.get(i).x, p.get(i).y);
            } else {
                if (p.isBezierStart(i)) {
                    path.curveTo(p.get(i).x, p.get(i).y, p.get(i + 1).x, p.get(i + 1).y, p.get(i + 2).x, p.get(i + 2).y);
                    i += 2;
                } else
                    path.lineTo(p.get(i).x, p.get(i).y);
            }
        }
        //CHECKSTYLE.ON: ModifiedControlVariable

        if (p.isClosed())
            path.closePath();

        if (style.isFilled() && p.isClosed())
            gr.fill(path);
        gr.draw(path);
    }

    @Override
    public void drawCircle(Vector p1, Vector p2, Style style) {
        Vector w = Vector.width(p1, p2);
        if (w.x > pixelSize || w.y > pixelSize) {
            applyStyle(style);
            Vector p = Vector.min(p1, p2);
            if (style.isFilled())
                gr.fillOval(p.x - 1, p.y - 1, w.x + 2, w.y + 2);
            else
                gr.drawOval(p.x, p.y, w.x, w.y);
        }
    }

    private void applyStyle(Style style) {
        if (style != lastStyle) {
            gr.setStroke(style.getStroke());
            gr.setColor(style.getColor());
            gr.setFont(style.getFont());
            lastStyle = style;
        }
    }

    @Override
    public void drawText(Vector p1, Vector p2, String text, Orientation orientation, Style style) {
        applyStyle(style); // sets also font size!
        int height = gr.getFontMetrics().getHeight();
        if (height > minFontSize) {
            if (text == null || text.length() == 0) return;
            text = text.replace('~', '¬');

            //GraphicMinMax.approxTextSize(this, p1, p2, text, orientation, style);

            text = cleanLabel(text);

            boolean rotateText = false;
            if (p1.y == p2.y) {   // 0 and 180 deg
                if (p1.x > p2.x)   // 180
                    orientation = orientation.rot(2);
            } else {
                if (p1.y < p2.y) // 270
                    orientation = orientation.rot(2);
                else            // 90
                    orientation = orientation.rot(0);
                rotateText = true;
            }

            AffineTransform old = null;
            if (rotateText) {
                old = gr.getTransform();
                gr.translate(p1.x, p1.y);
                gr.rotate(-Math.PI / 2);
                gr.translate(-p1.x, -p1.y);
            }

            int xoff = 0;
            if (orientation.getX() != 0) {
                int width = gr.getFontMetrics().stringWidth(text);
                xoff -= width * orientation.getX() / 2;
            }

            int yoff = 0;
            if (orientation.getY() != 0) {
                yoff += height * orientation.getY() / 3;
            }

            gr.drawString(text, p1.x + xoff, p1.y + yoff);

            if (rotateText)
                gr.setTransform(old);
        }
    }

}
