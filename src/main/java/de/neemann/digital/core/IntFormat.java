package de.neemann.digital.core;

/**
 * @author hneemann
 */
public enum IntFormat {
    /**
     * the default format
     */
    def,
    /**
     * decimal
     */
    dec,
    /**
     * decimal signed
     */
    decSigned,
    /**
     * hexadecimal
     */
    hex,
    /**
     * binary
     */
    bin,
    /**
     * ascii format
     */
    ascii;

    /**
     * Formats the value.
     * Uses this method to create a string which is only shown to the user.
     * If the user is able to edit the string use {@link IntFormat#formatToEdit(Value)} instead.
     *
     * @param inValue the value to format
     * @return the formatted value as a string
     */
    public String formatToView(Value inValue) {
        if (inValue.isHighZ())
            return "?";

        switch (this) {
            case dec:
                return Long.toString(inValue.getValue());
            case decSigned:
                return Long.toString(inValue.getValueSigned());
            case hex:
                return toHex(inValue);
            case bin:
                return toBin(inValue);
            case ascii:
                return "" + (char) inValue.getValue();
            default:
                return toShortHex(inValue.getValue(), false);
        }
    }

    /**
     * Formats the value.
     * Creates a string which can be parsed by {@link Bits#decode(String)}
     *
     * @param inValue the value to format
     * @return the formatted value as a string
     * @see Bits#decode(String)
     */
    public String formatToEdit(Value inValue) {
        if (inValue.isHighZ())
            return "?";

        switch (this) {
            case dec:
                return Long.toString(inValue.getValue());
            case decSigned:
                return Long.toString(inValue.getValueSigned());
            case hex:
                return "0x" + toHex(inValue);
            case bin:
                return "0b" + toBin(inValue);
            case ascii:
                return "'" + (char) inValue.getValue() + "'";
            default:
                final long value = inValue.getValue();
                if (value >= 0 && value < 10)
                    return toShortHex(value, true);
                else
                    return "0x" + toShortHex(value, true);
        }
    }


    private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static String toHex(Value inValue) {
        final int bits = inValue.getBits();
        final int numChars = (bits - 1) / 4 + 1;

        StringBuilder sb = new StringBuilder(numChars);
        final long value = inValue.getValue();
        for (int i = numChars - 1; i >= 0; i--) {
            int c = (int) ((value >> (i * 4)) & 0xf);
            sb.append(DIGITS[c]);
        }
        return sb.toString();
    }


    /**
     * Creates a short hex representation of the given value.
     * Use only to represent a value.
     * If confusion is excluded, the prefix '0x' is omitted.
     * Thus 0x1A3 is converted to "1A3" which can not be parsed back to a long because "0x" is missing.
     *
     * @param value the value
     * @return the hex string
     */
    public static String toShortHex(long value) {
        return toShortHex(value, false);
    }

    private static final int BUF = 16;

    private static String toShortHex(long value, boolean omitPrefix) {
        if (value == 0)
            return "0";

        boolean wasChar = false;
        int p = BUF;
        char[] data = new char[BUF];
        while (value != 0) {
            final int d = (int) (value & 0xf);
            if (d >= 10) wasChar = true;
            p--;
            data[p] = DIGITS[d];
            value >>>= 4;
        }

        if (omitPrefix || wasChar || p == BUF - 1)
            return new String(data, p, BUF - p);
        else
            return "0x" + new String(data, p, BUF - p);
    }

    private static String toBin(Value inValue) {
        final int bits = inValue.getBits();
        char[] data = new char[bits];
        final long value = inValue.getValue();
        long mask = 1;
        for (int i = bits - 1; i >= 0; i--) {
            if ((value & mask) != 0)
                data[i] = '1';
            else
                data[i] = '0';
            mask <<= 1;
        }
        return new String(data);
    }
}
