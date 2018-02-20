package de.neemann.digital.analyse.expression;

import junit.framework.TestCase;

/**
 * @author hneemann
 */
public class BitSetterTest extends TestCase {

    private BitSetter bs;

    public void testGetSet() {
        bs = new BitSetter(4) {
            @Override
            public void setBit(int row, int bit, boolean value) {
                check(row, bit, value);
            }
        };

        for (int i = 0; i < 16; i++)
            bs.fill(i);
    }

    private void check(int row, int bit, boolean value) {
        assertEquals(value, bs.getBit(row, bit));
    }

}