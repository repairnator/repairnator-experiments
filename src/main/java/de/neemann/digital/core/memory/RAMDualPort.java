package de.neemann.digital.core.memory;

import de.neemann.digital.core.*;
import de.neemann.digital.core.element.Element;
import de.neemann.digital.core.element.ElementAttributes;
import de.neemann.digital.core.element.ElementTypeDescription;
import de.neemann.digital.core.element.Keys;

import static de.neemann.digital.core.element.PinInfo.input;

/**
 * RAM module with different ports to read and write the data.
 *
 * @author hneemann
 */
public class RAMDualPort extends Node implements Element, RAMInterface {

    /**
     * The RAMs {@link ElementTypeDescription}
     */
    public static final ElementTypeDescription DESCRIPTION = new ElementTypeDescription(RAMDualPort.class,
            input("A"),
            input("Din"),
            input("str"),
            input("C").setClock(),
            input("ld"))
            .addAttribute(Keys.ROTATE)
            .addAttribute(Keys.BITS)
            .addAttribute(Keys.ADDR_BITS)
            .addAttribute(Keys.LABEL);

    private final DataField memory;
    private final ObservableValue output;
    private final int addrBits;
    private final int bits;
    private final String label;
    private final int size;
    private ObservableValue addrIn;
    private ObservableValue dataIn;
    private ObservableValue strIn;
    private ObservableValue clkIn;
    private ObservableValue ldIn;
    private int addr;
    private boolean lastClk = false;
    private boolean ld;

    /**
     * Creates a new instance
     *
     * @param attr the elemets attributes
     */
    public RAMDualPort(ElementAttributes attr) {
        super(true);
        bits = attr.get(Keys.BITS);
        output = createOutput();
        addrBits = attr.get(Keys.ADDR_BITS);
        size = 1 << addrBits;
        memory = new DataField(size);
        label = attr.getCleanLabel();
    }

    /**
     * called to create the output value
     *
     * @return the output value
     */
    protected ObservableValue createOutput() {
        return new ObservableValue("D", bits, true).setPinDescription(DESCRIPTION);
    }

    @Override
    public void setInputs(ObservableValues inputs) throws NodeException {
        setAddrIn(inputs.get(0));
        setDataIn(inputs.get(1));
        setStrIn(inputs.get(2));
        setClkIn(inputs.get(3));
        setLdIn(inputs.get(4));
    }

    /**
     * Sets the addrIn input value
     *
     * @param addrIn addrIn
     * @throws BitsException BitsException
     */
    protected void setAddrIn(ObservableValue addrIn) throws BitsException {
        this.addrIn = addrIn.checkBits(addrBits, this).addObserverToValue(this);
    }

    /**
     * Sets the dataIn input value
     *
     * @param dataIn dataIn
     * @throws BitsException BitsException
     */
    protected void setDataIn(ObservableValue dataIn) throws BitsException {
        this.dataIn = dataIn.checkBits(bits, this);
    }

    /**
     * Sets the strIn input value
     *
     * @param strIn strIn
     * @throws BitsException BitsException
     */
    protected void setStrIn(ObservableValue strIn) throws BitsException {
        this.strIn = strIn.checkBits(1, this);
    }

    /**
     * Sets the clkIn input value
     *
     * @param clkIn clkIn
     * @throws BitsException BitsException
     */
    protected void setClkIn(ObservableValue clkIn) throws BitsException {
        this.clkIn = clkIn.checkBits(1, this).addObserverToValue(this);
    }

    /**
     * Sets the ldIn input value
     *
     * @param ldIn ldIn
     * @throws BitsException BitsException
     */
    protected void setLdIn(ObservableValue ldIn) throws BitsException {
        this.ldIn = ldIn.checkBits(1, this).addObserverToValue(this);
    }

    @Override
    public ObservableValues getOutputs() {
        return output.asList();
    }

    @Override
    public void readInputs() throws NodeException {
        long data = 0;
        boolean clk = clkIn.getBool();
        boolean str;
        if (!lastClk && clk) {
            str = strIn.getBool();
            if (str)
                data = dataIn.getValue();
        } else
            str = false;
        ld = ldIn.getBool();
        if (ld || str)
            addr = (int) addrIn.getValue();

        if (str)
            memory.setData(addr, data);

        lastClk = clk;
    }

    @Override
    public void writeOutputs() throws NodeException {
        if (ld) {
            output.set(memory.getDataWord(addr), false);
        } else {
            output.set(0, true);
        }
    }

    @Override
    public DataField getMemory() {
        return memory;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getDataBits() {
        return bits;
    }

    @Override
    public int getAddrBits() {
        return addrBits;
    }
}
