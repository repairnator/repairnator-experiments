package de.neemann.digital.testing;

import de.neemann.digital.data.Value;
import de.neemann.digital.testing.parser.LineListener;

import java.util.ArrayList;

/**
 * Resolves don't cares in the inputs list
 * Created by hneemann on 19.04.17.
 */
public class LineListenerResolveDontCare implements LineListener {

    private final LineListener parent;
    private final ArrayList<TestExecutor.TestSignal> inputs;

    /**
     * Create a new instance
     *
     * @param parent the parent listener
     * @param inputs the input test signals
     */
    public LineListenerResolveDontCare(LineListener parent, ArrayList<TestExecutor.TestSignal> inputs) {
        this.parent = parent;
        this.inputs = inputs;
    }

    @Override
    public void add(Value[] row) {
        ArrayList<Integer> dcIndex = null;
        for (TestExecutor.TestSignal in : inputs) {
            if (row[in.getIndex()].getType() == Value.Type.DONTCARE) {
                if (dcIndex == null)
                    dcIndex = new ArrayList<>();
                dcIndex.add(in.getIndex());
            }
        }
        if (dcIndex == null)
            parent.add(row);
        else {
            int count = 1 << dcIndex.size();
            for (int n = 0; n < count; n++) {
                int mask = 1;
                for (int in : dcIndex) {
                    boolean val = (n & mask) != 0;
                    row[in] = new Value(val ? 1 : 0);
                    mask *= 2;
                }
                parent.add(row);
            }
        }
    }
}
