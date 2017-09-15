/*
 * Copyright 2015 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.socket.InternetProtocolFamily2;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.UnstableApi;

import static io.netty.handler.codec.dns.DefaultDnsRecordDecoder.ROOT;

/**
 * The default {@link DnsRecordEncoder} implementation.
 *
 * @see DefaultDnsRecordDecoder
 */
@UnstableApi
public class DefaultDnsRecordEncoder implements DnsRecordEncoder {
    private static final int PREFIX_MASK = Byte.SIZE - 1;

    /**
     * Creates a new instance.
     */
    protected DefaultDnsRecordEncoder() { }

    @Override
    public final void encodeQuestion(DnsQuestion question, ByteBuf out) throws Exception {
        encodeName(question.name(), out);
        out.writeShort(question.type().intValue());
        out.writeShort(question.dnsClass());
    }

    @Override
    public void encodeRecord(DnsRecord record, ByteBuf out) throws Exception {
        if (record instanceof DnsQuestion) {
            encodeQuestion((DnsQuestion) record, out);
        } else if (record instanceof DnsPtrRecord) {
            encodePtrRecord((DnsPtrRecord) record, out);
        } else if (record instanceof DnsOptEcsRecord) {
            encodeOptEcsRecord((DnsOptEcsRecord) record, out);
        } else if (record instanceof DnsOptPseudoRecord) {
            encodeOptPseudoRecord((DnsOptPseudoRecord) record, out);
        } else if (record instanceof DnsRawRecord) {
            encodeRawRecord((DnsRawRecord) record, out);
        } else {
            throw new UnsupportedMessageTypeException(StringUtil.simpleClassName(record));
        }
    }

    private void encodeRecord0(DnsRecord record, ByteBuf out) throws Exception {
        encodeName(record.name(), out);
        out.writeShort(record.type().intValue());
        out.writeShort(record.dnsClass());
        out.writeInt((int) record.timeToLive());
    }

    private void encodePtrRecord(DnsPtrRecord record, ByteBuf out) throws Exception {
        encodeRecord0(record, out);
        encodeName(record.hostname(), out);
    }

    private void encodeOptPseudoRecord(DnsOptPseudoRecord record, ByteBuf out) throws Exception {
        encodeRecord0(record, out);
        out.writeShort(0);
    }

    private void encodeOptEcsRecord(DnsOptEcsRecord record, ByteBuf out) throws Exception {
        encodeRecord0(record, out);

        int sourcePrefixLength = record.sourcePrefixLength();
        int scopePrefixLength = record.scopePrefixLength();
        int lowOrderBitsToPreserve = sourcePrefixLength & PREFIX_MASK;

        byte[] bytes = record.address();
        int addressBits = bytes.length << 3;
        if (addressBits < sourcePrefixLength || sourcePrefixLength < 0) {
            throw new IllegalArgumentException(sourcePrefixLength + ": " +
                    sourcePrefixLength + " (expected: 0 >= " + addressBits + ')');
        }

        // See http://www.iana.org/assignments/address-family-numbers/address-family-numbers.xhtml
        final short addressNumber = (short) (bytes.length == 4 ?
                InternetProtocolFamily2.IPv4.addressNumber() : InternetProtocolFamily2.IPv6.addressNumber());
        int payloadLength = calculateEcsAddressLength(sourcePrefixLength, lowOrderBitsToPreserve);

        int fullPayloadLength = 2 + // OPTION-CODE
                2 + // OPTION-LENGTH
                2 + // FAMILY
                1 + // SOURCE PREFIX-LENGTH
                1 + // SCOPE PREFIX-LENGTH
                payloadLength; //  ADDRESS...

        out.writeShort(fullPayloadLength);
        out.writeShort(8); // This is the defined type for ECS.

        out.writeShort(fullPayloadLength - 4); // Not include OPTION-CODE and OPTION-LENGTH
        out.writeShort(addressNumber);
        out.writeByte(sourcePrefixLength);
        out.writeByte(scopePrefixLength); // Must be 0 in queries.

        if (lowOrderBitsToPreserve > 0) {
            int bytesLength = payloadLength - 1;
            out.writeBytes(bytes, 0, bytesLength);

            // Pad the leftover of the last byte with zeros.
            out.writeByte(padWithZeros(bytes[bytesLength], lowOrderBitsToPreserve));
        } else {
            // The sourcePrefixLength align with Byte so just copy in the bytes directly.
            out.writeBytes(bytes, 0, payloadLength);
        }
    }

    // Package-Private for testing
    static int calculateEcsAddressLength(int sourcePrefixLength, int lowOrderBitsToPreserve) {
        return (sourcePrefixLength >>> 3) + (lowOrderBitsToPreserve != 0 ? 1 : 0);
    }

    private void encodeRawRecord(DnsRawRecord record, ByteBuf out) throws Exception {
        encodeRecord0(record, out);

        ByteBuf content = record.content();
        int contentLen = content.readableBytes();

        out.writeShort(contentLen);
        out.writeBytes(content, content.readerIndex(), contentLen);
    }

    protected void encodeName(String name, ByteBuf buf) throws Exception {
        if (ROOT.equals(name)) {
            // Root domain
            buf.writeByte(0);
            return;
        }

        final String[] labels = name.split("\\.");
        for (String label : labels) {
            final int labelLen = label.length();
            if (labelLen == 0) {
                // zero-length label means the end of the name.
                break;
            }

            buf.writeByte(labelLen);
            ByteBufUtil.writeAscii(buf, label);
        }

        buf.writeByte(0); // marks end of name field
    }

    // Package private so it can be reused in the test.
    static byte padWithZeros(byte b, int lowOrderBitsToPreserve) {
        switch (lowOrderBitsToPreserve) {
        case 0:
            return 0;
        case 1:
            return (byte) (0x01 & b);
        case 2:
            return (byte) (0x03 & b);
        case 3:
            return (byte) (0x07 & b);
        case 4:
            return (byte) (0x0F & b);
        case 5:
            return (byte) (0x1F & b);
        case 6:
            return (byte) (0x3F & b);
        case 7:
            return (byte) (0x7F & b);
        case 8:
            return b;
        default:
            throw new IllegalArgumentException("lowOrderBitsToPreserve: " + lowOrderBitsToPreserve);
        }
    }
}
