package de.neemann.digital.core.wiring;

import de.neemann.digital.core.Node;
import de.neemann.digital.core.NodeException;
import de.neemann.digital.core.ObservableValue;
import de.neemann.digital.core.ObservableValues;
import de.neemann.digital.core.element.*;
import de.neemann.digital.draw.elements.PinException;
import de.neemann.digital.lang.Lang;

import static de.neemann.digital.core.element.PinInfo.input;

/**
 * The PriorityEncoder
 *
 * @author hneemann
 */
public class PriorityEncoder extends Node implements Element {

    /**
     * The PriorityEncoder description
     */
    public static final ElementTypeDescription DESCRIPTION = new ElementTypeDescription(PriorityEncoder.class) {
        @Override
        public PinDescriptions getInputDescription(ElementAttributes elementAttributes) {
            int inputs = 1 << elementAttributes.get(Keys.SELECTOR_BITS);
            PinDescription[] names = new PinDescription[inputs];
            for (int i = 0; i < inputs; i++)
                names[i] = input("in" + i, Lang.get("elem_PriorityEncoder_input", i));
            return new PinDescriptions(names);
        }
    }
            .addAttribute(Keys.ROTATE)
            .addAttribute(Keys.LABEL)
            .addAttribute(Keys.SELECTOR_BITS);


    private final ObservableValue selOut;
    private final ObservableValue anyOut;
    private final int inputCount;
    private ObservableValues inputs;
    private long sel;
    private boolean any;

    /**
     * Creates a new instance
     *
     * @param attributes the attributes
     */
    public PriorityEncoder(ElementAttributes attributes) {
        final int outBits = attributes.get(Keys.SELECTOR_BITS);
        this.inputCount = 1 << outBits;
        selOut = new ObservableValue("num", outBits).setPinDescription(DESCRIPTION);
        anyOut = new ObservableValue("any", 1).setPinDescription(DESCRIPTION);
    }

    @Override
    public void readInputs() throws NodeException {
        sel = 0;
        any = false;
        for (int i = 0; i < inputCount; i++)
            if (inputs.get(i).getBool()) {
                sel = i;
                any = true;
            }
    }

    @Override
    public void writeOutputs() throws NodeException {
        selOut.setValue(sel);
        anyOut.setBool(any);
    }

    @Override
    public void setInputs(ObservableValues inputs) throws NodeException {
        this.inputs = inputs;
        for (int i = 0; i < inputCount; i++)
            inputs.get(i).addObserverToValue(this).checkBits(1, this, i);
    }

    @Override
    public ObservableValues getOutputs() throws PinException {
        return new ObservableValues(selOut, anyOut);
    }
}
