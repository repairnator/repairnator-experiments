package com.firefly.codec.http2.decode;

import java.nio.ByteBuffer;

import com.firefly.codec.http2.frame.ErrorCode;
import com.firefly.codec.http2.frame.Flags;
import com.firefly.codec.http2.frame.PushPromiseFrame;
import com.firefly.codec.http2.model.MetaData;

public class PushPromiseBodyParser extends BodyParser {
	private final HeaderBlockParser headerBlockParser;
	private State state = State.PREPARE;
	private int cursor;
	private int length;
	private int paddingLength;
	private int streamId;

	public PushPromiseBodyParser(HeaderParser headerParser, Parser.Listener listener,
			HeaderBlockParser headerBlockParser) {
		super(headerParser, listener);
		this.headerBlockParser = headerBlockParser;
	}

	private void reset() {
		state = State.PREPARE;
		cursor = 0;
		length = 0;
		paddingLength = 0;
		streamId = 0;
	}

	@Override
	public boolean parse(ByteBuffer buffer) {
		boolean loop = false;
		while (buffer.hasRemaining() || loop) {
			switch (state) {
			case PREPARE: {
				// SPEC: wrong streamId is treated as connection error.
				if (getStreamId() == 0)
					return connectionFailure(buffer, ErrorCode.PROTOCOL_ERROR.code, "invalid_push_promise_frame");

				// For now we don't support PUSH_PROMISE frames that don't have
				// END_HEADERS.
				if (!hasFlag(Flags.END_HEADERS))
					return connectionFailure(buffer, ErrorCode.INTERNAL_ERROR.code, "unsupported_push_promise_frame");

				length = getBodyLength();

				if (isPadding()) {
					state = State.PADDING_LENGTH;
				} else {
					state = State.STREAM_ID;
				}
				break;
			}
			case PADDING_LENGTH: {
				paddingLength = buffer.get() & 0xFF;
				--length;
				length -= paddingLength;
				state = State.STREAM_ID;
				if (length < 4)
					return connectionFailure(buffer, ErrorCode.FRAME_SIZE_ERROR.code, "invalid_push_promise_frame");
				break;
			}
			case STREAM_ID: {
				if (buffer.remaining() >= 4) {
					streamId = buffer.getInt();
					streamId &= 0x7F_FF_FF_FF;
					length -= 4;
					state = State.HEADERS;
					loop = length == 0;
				} else {
					state = State.STREAM_ID_BYTES;
					cursor = 4;
				}
				break;
			}
			case STREAM_ID_BYTES: {
				int currByte = buffer.get() & 0xFF;
				--cursor;
				streamId += currByte << (8 * cursor);
				--length;
				if (cursor > 0 && length <= 0)
					return connectionFailure(buffer, ErrorCode.FRAME_SIZE_ERROR.code, "invalid_push_promise_frame");
				if (cursor == 0) {
					streamId &= 0x7F_FF_FF_FF;
					state = State.HEADERS;
					loop = length == 0;
				}
				break;
			}
			case HEADERS: {
				MetaData metaData = headerBlockParser.parse(buffer, length);
				if (metaData != null) {
					state = State.PADDING;
					loop = paddingLength == 0;
					onPushPromise(streamId, metaData);
				}
				break;
			}
			case PADDING: {
				int size = Math.min(buffer.remaining(), paddingLength);
				buffer.position(buffer.position() + size);
				paddingLength -= size;
				if (paddingLength == 0) {
					reset();
					return true;
				}
				break;
			}
			default: {
				throw new IllegalStateException();
			}
			}
		}
		return false;
	}

	private void onPushPromise(int streamId, MetaData metaData) {
		PushPromiseFrame frame = new PushPromiseFrame(getStreamId(), streamId, metaData);
		notifyPushPromise(frame);
	}

	private enum State {
		PREPARE, PADDING_LENGTH, STREAM_ID, STREAM_ID_BYTES, HEADERS, PADDING
	}
}
