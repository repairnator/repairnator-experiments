/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.jersey.oauth1.signature;

import java.io.IOException;

import org.glassfish.jersey.oauth1.signature.internal.LocalizationMessages;

/**
 * Base64 encoding util class.
 *
 */
public class Base64 {

    private static final char ALPHABET[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};

    private static int valueDecoding[] = new int[128];

    static {

        for (int i = 0; i < valueDecoding.length; i++) {
            valueDecoding[i] = -1;
        }

        for (int i = 0; i < ALPHABET.length; i++) {
            valueDecoding[ALPHABET[i]] = i;
        }
    }

    /**
     * Converts a byte array into a Base64 encoded string.
     *
     * @param data bytes to encode.
     * @return base64 encoding of data; 4 chars for every 3 bytes.
     */
    public static String encode(byte[] data) {
        return encode(data, 0, data.length);
    }

    /**
     * Converts a byte array into a Base64 encoded string.
     *
     * @param data bytes to encode.
     * @param offset which byte to start at.
     * @param length how many bytes to encode; padding will be added if needed.
     * @return base64 encoding of data; 4 chars for every 3 bytes.
     */
    public static String encode(byte[] data, int offset, int length) {

        int i;
        int encodedLen;
        char[] encoded;

        // 4 chars for 3 bytes, run input up to a multiple of 3
        encodedLen = (length + 2) / 3 * 4;
        encoded = new char[encodedLen];

        for (i = 0, encodedLen = 0; encodedLen < encoded.length; i += 3, encodedLen += 4) {
            encodeQuantum(data, offset + i, length - i, encoded, encodedLen);
        }

        return new String(encoded);
    }

    /**
     * Encodes 1, 2, or 3 bytes of data as 4 Base64 chars.
     *
     * @param in buffer of bytes to encode.
     * @param inOffset where the first byte to encode is.
     * @param len how many bytes to encode.
     * @param out buffer to put the output in.
     * @param outOffset where in the output buffer to put the chars.
     */
    private static void encodeQuantum(byte in[], int inOffset, int len, char out[], int outOffset) {

        byte a = 0, b = 0, c = 0;

        a = in[inOffset];
        out[outOffset] = ALPHABET[(a >>> 2) & 0x3F];

        if (len > 2) {
            b = in[inOffset + 1];
            c = in[inOffset + 2];
            out[outOffset + 1] = ALPHABET[((a << 4) & 0x30) + ((b >>> 4) & 0xf)];
            out[outOffset + 2] = ALPHABET[((b << 2) & 0x3c) + ((c >>> 6) & 0x3)];
            out[outOffset + 3] = ALPHABET[c & 0x3F];
        } else if (len > 1) {
            b = in[inOffset + 1];
            out[outOffset + 1] = ALPHABET[((a << 4) & 0x30) + ((b >>> 4) & 0xf)];
            out[outOffset + 2] = ALPHABET[((b << 2) & 0x3c) + ((c >>> 6) & 0x3)];
            out[outOffset + 3] = '=';
        } else {
            out[outOffset + 1] = ALPHABET[((a << 4) & 0x30) + ((b >>> 4) & 0xf)];
            out[outOffset + 2] = '=';
            out[outOffset + 3] = '=';
        }
    }

    /**
     * Converts a Base64 encoded string to a byte array.
     *
     * @param encoded Base64 encoded data.
     * @return decode binary data; 3 bytes for every 4 chars - minus padding.
     * @throws java.io.IOException if an I/O error occurs reading the data.
     */
    public static byte[] decode(String encoded) throws IOException {
        return decode(encoded, 0, encoded.length());
    }

    /**
     * Converts an embedded Base64 encoded string to a byte array.
     *
     * @param encoded a String with Base64 data embedded in it.
     * @param offset which char of the String to start at.
     * @param length how many chars to decode; must be a multiple of 4.
     * @return decode binary data; 3 bytes for every 4 chars - minus padding.
     * @throws java.io.IOException if an I/O error occurs reading the data.
     */
    public static byte[] decode(String encoded, int offset, int length) throws IOException {

        int i;
        int decodedLen;
        byte[] decoded;

        // the input must be a multiple of 4
        if (length % 4 != 0) {
            throw new IOException(LocalizationMessages.ERROR_BASE_64_LENGTH());
        }

        // 4 chars for 3 bytes, but there may have been pad bytes
        decodedLen = length / 4 * 3;
        if (encoded.charAt(offset + length - 1) == '=') {
            decodedLen--;
            if (encoded.charAt(offset + length - 2) == '=') {
                decodedLen--;
            }
        }

        decoded = new byte[decodedLen];

        for (i = 0, decodedLen = 0; i < length; i += 4, decodedLen += 3) {
            decodeQuantum(encoded.charAt(offset + i),
                    encoded.charAt(offset + i + 1),
                    encoded.charAt(offset + i + 2),
                    encoded.charAt(offset + i + 3),
                    decoded, decodedLen);
        }

        return decoded;
    }

    /**
     * Decode 4 Base64 chars as 1, 2, or 3 bytes of data.
     *
     * @param in1 first char of quantum to decode.
     * @param in2 second char of quantum to decode.
     * @param in3 third char of quantum to decode.
     * @param in4 forth char of quantum to decode.
     * @param out buffer to put the output in.
     * @param outOffset where in the output buffer to put the bytes.
     */
    private static void decodeQuantum(char in1, char in2, char in3, char in4, byte[] out, int outOffset)
            throws IOException {

        int a = 0, b = 0, c = 0, d = 0;
        int pad = 0;

        a = valueDecoding[in1 & 127];
        b = valueDecoding[in2 & 127];

        if (in4 == '=') {
            pad++;
            if (in3 == '=') {
                pad++;
            } else {
                c = valueDecoding[in3 & 127];
            }
        } else {
            c = valueDecoding[in3 & 127];
            d = valueDecoding[in4 & 127];
        }

        if (a < 0 || b < 0 || c < 0 || d < 0) {
            throw new IOException(LocalizationMessages.ERROR_BASE_64_LENGTH());
        }

        // the first byte is the 6 bits of a and 2 bits of b
        out[outOffset] = (byte) (((a << 2) & 0xfc) | ((b >>> 4) & 3));

        if (pad < 2) {

            // the second byte is 4 bits of b and 4 bits of c
            out[outOffset + 1] = (byte) (((b << 4) & 0xf0) | ((c >>> 2) & 0xf));

            if (pad < 1) {
                // the third byte is 2 bits of c and 4 bits of d
                out[outOffset + 2] = (byte) (((c << 6) & 0xc0) | (d & 0x3f));
            }
        }
    }
}

