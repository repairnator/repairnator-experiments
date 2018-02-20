package de.neemann.digital.draw.graphics;

import java.io.Closeable;
import java.io.IOException;

/**
 * Interface used to draw the circuit.
 * There are implementations to draw on a {@link java.awt.Graphics2D} instance ({@link GraphicSwing}) but also
 * implementations which create export formats like SVG ({@link GraphicSVG}).
 *
 * @author hneemann
 */
public interface Graphic extends Closeable {
    /**
     * Flag to enable LaTeX output mode.
     */
    String LATEX = "LaTeX";

    /**
     * Sets the bounding box of the future usage of this instance
     * Instances that create a file will use this bounding box th write a header.
     * So this method needs to be called before a draw-Method is called.
     *
     * @param min upper left corner
     * @param max lower right corner
     * @return this for chained calls
     */
    default Graphic setBoundingBox(Vector min, Vector max) {
        return this;
    }

    /**
     * Draws a line
     *
     * @param p1    first point
     * @param p2    second point
     * @param style the line style
     */
    void drawLine(Vector p1, Vector p2, Style style);

    /**
     * Draws a polygon
     *
     * @param p     the polygon to draw
     * @param style the style
     */
    void drawPolygon(Polygon p, Style style);

    /**
     * Draws a circle
     *
     * @param p1    upper left corner of outer rectangle containing the circle
     * @param p2    lower right corner of outer rectangle containing the circle
     * @param style the style
     */
    void drawCircle(Vector p1, Vector p2, Style style);

    /**
     * Draws text
     *
     * @param p1          point to draw the text
     * @param p2          point at the left of p1, is used to determine the correct orientation of the text after transforming coordinates
     * @param text        the text
     * @param orientation the text orientation
     * @param style       the text style
     */
    void drawText(Vector p1, Vector p2, String text, Orientation orientation, Style style);

    /**
     * opens a new group, used to create SVG grouping
     */
    default void openGroup() {
    }

    /**
     * closes a group, used to create SVG grouping
     */
    default void closeGroup() {
    }

    /**
     * Returns true if the given flag is set
     *
     * @param name the flags name
     * @return true if the given flag is set
     */
    default boolean isFlagSet(String name) {
        return false;
    }


    /**
     * closes the graphics instance
     *
     * @throws IOException IOException
     */
    default void close() throws IOException {
    }
}
