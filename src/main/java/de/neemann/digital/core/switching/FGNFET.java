package de.neemann.digital.core.switching;

import de.neemann.digital.core.ObservableValue;
import de.neemann.digital.core.element.ElementAttributes;
import de.neemann.digital.core.element.ElementTypeDescription;
import de.neemann.digital.core.element.Keys;

import static de.neemann.digital.core.element.PinInfo.input;

/**
 * N-Channel floating gate MOS FET
 * Created by hneemann on 22.02.17.
 */
public class FGNFET extends NFET {
    /**
     * The switch description
     */
    public static final ElementTypeDescription DESCRIPTION = new ElementTypeDescription(FGNFET.class, input("G"))
            .addAttribute(Keys.ROTATE)
            .addAttribute(Keys.BITS)
            .addAttribute(Keys.LABEL)
            .addAttribute(Keys.BLOWN);

    private final boolean programmed;

    /**
     * Create a new instance
     *
     * @param attr the attributes
     */
    public FGNFET(ElementAttributes attr) {
        super(attr, false);
        getOutput1().setPinDescription(DESCRIPTION);
        getOutput2().setPinDescription(DESCRIPTION);
        programmed = attr.get(Keys.BLOWN);
    }

    @Override
    boolean getClosed(ObservableValue input) {
        if (input.isHighZ() || programmed)
            return false;
        else
            return input.getBool();
    }
}
