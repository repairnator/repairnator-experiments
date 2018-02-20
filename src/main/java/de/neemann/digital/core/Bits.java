package de.neemann.digital.core;

import de.neemann.digital.lang.Lang;

/**
 * Helper for bit manipulating
 */
public final class Bits {

    private Bits() {
    }

    /**
     * Shifts a value up
     *
     * @param val  the value to shift
     * @param bits the bit count to shift
     * @return the shifted value
     */
    public static long up(long val, int bits) {
        if (bits < 64)
            return val << bits;
        else
            return 0;
    }

    /**
     * Shifts a value down
     *
     * @param val  the value to shift
     * @param bits the bit count to shift
     * @return the shifted value
     */
    public static long down(long val, int bits) {
        if (bits < 64)
            return val >>> bits;
        else
            return 0;
    }

    /**
     * Creates a bit mask with the lowest [bits] bits set.
     *
     * @param bits the number of 1 bits
     * @return a value with the lowest [bits] bits set.
     */
    public static long mask(int bits) {
        if (bits < 64)
            return (1L << bits) - 1;
        else
            return -1;
    }

    /**
     * calculates the signed flag.
     *
     * @param bits the bit count
     * @return the last used bit ( 1<<(bits-1) )
     */
    public static long signedFlagMask(int bits) {
        return up(1, bits - 1);
    }

    /**
     * Returns true if value is negative
     *
     * @param value the value
     * @param bits  the bit count
     * @return true if the last relevant bit is set
     */
    public static boolean isNegative(long value, int bits) {
        return (value & signedFlagMask(bits)) != 0;
    }

    /**
     * Sign extension of the value.
     * signExtend(3,2) returns -1.
     *
     * @param value the value
     * @param bits  number of bits
     * @return the sign extended value
     */
    public static long signExtend(long value, int bits) {
        if (bits >= 64)
            return value;
        else {
            if ((value & signedFlagMask(bits)) == 0)
                return value;
            else
                return value | ~mask(bits);
        }
    }

    /**
     * Calculates the number of bits needed to store the given value b.
     *
     * @param b number
     * @return number of bits needed to store b
     */
    public static int binLn2(int b) {
        int outBits = 1;
        while ((1 << outBits) <= b)
            outBits++;
        return outBits;
    }

    /**
     * Removes a bit from a value.
     * This means it shifts the higher bits down. Behaves like removing an item from a list.
     *
     * @param value the value
     * @param bit   the bit to remove
     * @return the new value
     */
    public static int removeBitFromValue(int value, int bit) {
        if (bit > 0) {
            return ((value & (~((1 << (bit + 1)) - 1))) >>> 1) | (value & ((1 << bit) - 1));
        } else {
            return value >>> 1;
        }
    }

    /**
     * Decodes a string to a long.
     * Supports decimal, octal, hex, binary and ascii
     *
     * @param str the string
     * @return the long value
     * @throws NumberFormatException invalid string
     */
    public static long decode(String str) throws NumberFormatException {
        if (str == null)
            return 0;

        str = str.trim();

        if (str.length() == 0)
            return 0;

        int p = 0;

        boolean neg = false;
        if (str.charAt(p) == '-') {
            neg = true;
            p++;
        }

        if (p >= str.length())
            throw new NumberFormatException(str, p);

        boolean wasZero = false;
        while (str.length() > p && str.charAt(p) == '0') {
            wasZero = true;
            p++;
        }

        if (p >= str.length())
            return 0;

        int radix;
        switch (str.charAt(p)) {
            case 'x':
            case 'X':
                if (neg) throw new NumberFormatException(str, p);
                radix = 16;
                p++;
                if (p == str.length()) throw new NumberFormatException(str, p);
                break;
            case 'b':
            case 'B':
                if (neg) throw new NumberFormatException(str, p);
                radix = 2;
                p++;
                if (p == str.length()) throw new NumberFormatException(str, p);
                break;
            case '\'':
                p++;
                if (p == str.length()) throw new NumberFormatException(str, p);
                return str.charAt(p);
            default:
                if (wasZero) {
                    if (neg) throw new NumberFormatException(str, p);
                    radix = 8;
                } else
                    radix = 10;
        }

        long val = decode(str, p, radix);

        if (neg)
            val = -val;
        return val;
    }

    /**
     * Decodes the given string starting at position p
     *
     * @param str   the string to decode
     * @param p     the starting position
     * @param radix the radix
     * @return the value
     * @throws NumberFormatException NumberFormatException
     */
    public static long decode(String str, int p, int radix) throws NumberFormatException {
        long val = 0;
        while (p < str.length()) {
            int d = Character.digit(str.charAt(p), radix);
            if (d < 0)
                throw new NumberFormatException(str, p);
            val = val * radix + d;
            p++;
        }
        return val;
    }

    /**
     * Indicates a invalid string.
     * Its not a runtime exception!
     */
    public final static class NumberFormatException extends Exception {
        private NumberFormatException(String str, int p) {
            super(Lang.get("err_invalidNumberFormat_N_N", str, p + 1));
        }
    }
}
