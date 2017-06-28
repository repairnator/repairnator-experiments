package org.corfudb.protocols.wireprotocol;

import com.google.common.collect.Range;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by mwei on 8/11/16.
 */
@Data
@AllArgsConstructor
public class ReadRequest implements ICorfuPayload<ReadRequest> {

    final Range<Long> range;
    @SuppressWarnings({"checkstyle:abbreviationaswordinname", "checkstyle:membername"})
    final UUID streamID;

    /**
     * Deserialization Constructor from ByteBuf to ReadRequest.
     *
     * @param buf The buffer to deserialize
     */
    public ReadRequest(ByteBuf buf) {
        range = ICorfuPayload.rangeFromBuffer(buf, Long.class);
        if (ICorfuPayload.fromBuffer(buf, Boolean.class)) {
            streamID = ICorfuPayload.fromBuffer(buf, UUID.class);
        } else {
            streamID = null;
        }
    }

    public ReadRequest(Long address) {
        range = Range.singleton(address);
        streamID = null;
    }

    @Override
    public void doSerialize(ByteBuf buf) {
        ICorfuPayload.serialize(buf, range);
        ICorfuPayload.serialize(buf, streamID != null);
        if (streamID != null) {
            ICorfuPayload.serialize(buf, streamID);
        }
    }

}
