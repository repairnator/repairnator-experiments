package com.firefly.codec.http2.stream;

import com.firefly.codec.http2.frame.DataFrame;
import com.firefly.codec.http2.frame.HeadersFrame;
import com.firefly.codec.http2.frame.PushPromiseFrame;
import com.firefly.codec.http2.frame.ResetFrame;
import com.firefly.utils.concurrent.Callback;
import com.firefly.utils.concurrent.Promise;

/**
 * <p>
 * A {@link Stream} represents a bidirectional exchange of data on top of a
 * {@link Session}.
 * </p>
 * <p>
 * Differently from socket streams, where the input and output streams are
 * permanently associated with the socket (and hence with the connection that
 * the socket represents), there can be multiple HTTP/2 streams present
 * concurrent for a HTTP/2 session.
 * </p>
 * <p>
 * A {@link Stream} maps to a HTTP request/response cycle, and after the
 * request/response cycle is completed, the stream is closed and removed from
 * the session.
 * </p>
 * <p>
 * Like {@link Session}, {@link Stream} is the active part and by calling its
 * API applications can generate events on the stream; conversely,
 * {@link Stream.Listener} is the passive part, and its callbacks are invoked
 * when events happen on the stream.
 * </p>
 *
 * @see Stream.Listener
 */
public interface Stream {
	/**
	 * @return the stream unique id
	 */
	public int getId();

	/**
	 * @return the session this stream is associated to
	 */
	public Session getSession();

	/**
	 * <p>
	 * Sends the given HEADERS {@code frame} representing a HTTP response.
	 * </p>
	 *
	 * @param frame
	 *            the HEADERS frame to send
	 * @param callback
	 *            the callback that gets notified when the frame has been sent
	 */
	public void headers(HeadersFrame frame, Callback callback);

	/**
	 * <p>
	 * Sends the given PUSH_PROMISE {@code frame}.
	 * </p>
	 *
	 * @param frame
	 *            the PUSH_PROMISE frame to send
	 * @param promise
	 *            the promise that gets notified of the pushed stream creation
	 * @param listener
	 *            the listener that gets notified of stream events
	 */
	public void push(PushPromiseFrame frame, Promise<Stream> promise, Listener listener);

	/**
	 * <p>
	 * Sends the given DATA {@code frame}.
	 * </p>
	 *
	 * @param frame
	 *            the DATA frame to send
	 * @param callback
	 *            the callback that gets notified when the frame has been sent
	 */
	public void data(DataFrame frame, Callback callback);

	/**
	 * <p>
	 * Sends the given RST_STREAM {@code frame}.
	 * </p>
	 *
	 * @param frame
	 *            the RST_FRAME to send
	 * @param callback
	 *            the callback that gets notified when the frame has been sent
	 */
	public void reset(ResetFrame frame, Callback callback);

	/**
	 * @param key
	 *            the attribute key
	 * @return an arbitrary object associated with the given key to this stream
	 *         or null if no object can be found for the given key.
	 * @see #setAttribute(String, Object)
	 */
	public Object getAttribute(String key);

	/**
	 * @param key
	 *            the attribute key
	 * @param value
	 *            an arbitrary object to associate with the given key to this
	 *            stream
	 * @see #getAttribute(String)
	 * @see #removeAttribute(String)
	 */
	public void setAttribute(String key, Object value);

	/**
	 * @param key
	 *            the attribute key
	 * @return the arbitrary object associated with the given key to this stream
	 * @see #setAttribute(String, Object)
	 */
	public Object removeAttribute(String key);

	/**
	 * @return whether this stream has been reset
	 */
	public boolean isReset();

	/**
	 * @return whether this stream is closed, both locally and remotely.
	 */
	public boolean isClosed();

	/**
	 * @return the stream idle timeout
	 * @see #setIdleTimeout(long)
	 */
	public long getIdleTimeout();

	/**
	 * @param idleTimeout
	 *            the stream idle timeout
	 * @see #getIdleTimeout()
	 */
	public void setIdleTimeout(long idleTimeout);

	/**
	 * <p>
	 * A {@link Stream.Listener} is the passive counterpart of a {@link Stream}
	 * and receives events happening on a HTTP/2 stream.
	 * </p>
	 *
	 * @see Stream
	 */
	public interface Listener {
		/**
		 * <p>
		 * Callback method invoked when a HEADERS frame representing the HTTP
		 * response has been received.
		 * </p>
		 *
		 * @param stream
		 *            the stream
		 * @param frame
		 *            the HEADERS frame received
		 */
		public void onHeaders(Stream stream, HeadersFrame frame);

		/**
		 * <p>
		 * Callback method invoked when a PUSH_PROMISE frame has been received.
		 * </p>
		 *
		 * @param stream
		 *            the stream
		 * @param frame
		 *            the PUSH_PROMISE frame received
		 * @return a {@link Stream.Listener} that will be notified of pushed
		 *         stream events
		 */
		public Listener onPush(Stream stream, PushPromiseFrame frame);

		/**
		 * <p>
		 * Callback method invoked when a DATA frame has been received.
		 * </p>
		 *
		 * @param stream
		 *            the stream
		 * @param frame
		 *            the DATA frame received
		 * @param callback
		 *            the callback to complete when the bytes of the DATA frame
		 *            have been consumed
		 */
		public void onData(Stream stream, DataFrame frame, Callback callback);

		/**
		 * <p>
		 * Callback method invoked when a RST_STREAM frame has been received for
		 * this stream.
		 * </p>
		 *
		 * @param stream
		 *            the stream
		 * @param frame
		 *            the RST_FRAME received
		 * @see Session.Listener#onReset(Session, ResetFrame)
		 */
		public void onReset(Stream stream, ResetFrame frame);

		/**
		 * <p>
		 * Callback method invoked when the stream exceeds its idle timeout.
		 * </p>
		 *
		 * @param stream
		 *            the stream
		 * @param x
		 *            the timeout failure
		 * @see #getIdleTimeout()
		 * @return true to reset the stream, false to ignore the idle timeout
		 */
		public boolean onIdleTimeout(Stream stream, Throwable x);

		/**
		 * <p>
		 * Empty implementation of {@link Listener}
		 * </p>
		 */
		public static class Adapter implements Listener {
			@Override
			public void onHeaders(Stream stream, HeadersFrame frame) {
			}

			@Override
			public Listener onPush(Stream stream, PushPromiseFrame frame) {
				return null;
			}

			@Override
			public void onData(Stream stream, DataFrame frame, Callback callback) {
				callback.succeeded();
			}

			@Override
			public void onReset(Stream stream, ResetFrame frame) {
			}

			@Override
			public boolean onIdleTimeout(Stream stream, Throwable x) {
				return true;
			}
		}
	}
}
