package de.neemann.digital.draw.shapes;

import de.neemann.digital.core.Observer;
import de.neemann.digital.core.element.ElementAttributes;
import de.neemann.digital.core.element.Keys;
import de.neemann.digital.core.element.PinDescriptions;
import de.neemann.digital.draw.elements.IOState;
import de.neemann.digital.draw.elements.Pin;
import de.neemann.digital.draw.elements.Pins;
import de.neemann.digital.draw.graphics.*;

import static de.neemann.digital.draw.shapes.GenericShape.SIZE2;
import static de.neemann.digital.draw.shapes.GenericShape.SIZE;

/**
 * The reset shape
 *
 * @author hneemann
 */
public class ResetShape implements Shape {

    private final String label;
    private final PinDescriptions outputs;
    private final boolean invOut;

    /**
     * Creates a new instance
     *
     * @param attr    the attributes
     * @param inputs  the inputs
     * @param outputs the outputs
     */
    public ResetShape(ElementAttributes attr, PinDescriptions inputs, PinDescriptions outputs) {
        this.outputs = outputs;
        this.label = attr.getLabel();
        invOut = attr.get(Keys.INVERT_OUTPUT);
    }

    @Override
    public Pins getPins() {
        return new Pins().add(new Pin(new Vector(0, 0), outputs.get(0)));
    }

    @Override
    public Interactor applyStateMonitor(IOState ioState, Observer guiObserver) {
        //ioState.getOutput(0).addObserverToValue(guiObserver);
        return null;
    }

    @Override
    public void drawTo(Graphic graphic, Style highLight) {
        int x = 0;
        if (invOut) {
            x -= SIZE - 1;
            graphic.drawCircle(new Vector(-SIZE + 1, -SIZE2 + 1),
                    new Vector(-1, SIZE2 - 1), Style.NORMAL);
        }

        graphic.drawPolygon(new Polygon(true)
                .add(x - OutputShape.SIZE * 2 - 2, -OutputShape.SIZE)
                .add(x - 2, -OutputShape.SIZE)
                .add(x - 2, OutputShape.SIZE)
                .add(x - OutputShape.SIZE * 2 - 2, OutputShape.SIZE), Style.NORMAL);

        Vector textPos = new Vector(x - OutputShape.SIZE * 2 + 2, -OutputShape.SIZE + 2);
        graphic.drawText(textPos, textPos.add(1, 0), "R", Orientation.LEFTTOP, Style.SHAPE_PIN);

        textPos = new Vector(x - OutputShape.SIZE * 3, 0);
        graphic.drawText(textPos, textPos.add(1, 0), label, Orientation.RIGHTCENTER, Style.NORMAL);
    }
}
