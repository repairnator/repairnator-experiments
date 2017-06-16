package com.firefly.client.http2;

import com.firefly.codec.http2.stream.HTTPConnection;
import com.firefly.net.DecoderChain;
import com.firefly.net.Session;
import com.firefly.net.tcp.ssl.SSLSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class ClientSecureDecoder extends DecoderChain {

	private static Logger log = LoggerFactory.getLogger("firefly-system");

	public ClientSecureDecoder(DecoderChain next) {
		super(next);
	}

	@Override
	public void decode(ByteBuffer buf, Session session) throws Throwable {
		if (session.getAttachment() instanceof HTTPConnection) {
			HTTPConnection connection = (HTTPConnection) session.getAttachment();
			ByteBuffer plaintext;

			switch (connection.getHttpVersion()) {
			case HTTP_2:
				plaintext = ((HTTP2ClientConnection) connection).getSSLSession().read(buf);
				break;
			case HTTP_1_1:
				plaintext = ((HTTP1ClientConnection) connection).getSSLSession().read(buf);
				break;
			default:
				throw new IllegalStateException(
						"client does not support the http version " + connection.getHttpVersion());
			}

			if (plaintext != null && next != null)
				next.decode(plaintext, session);
		} else if (session.getAttachment() instanceof SSLSession) {
			SSLSession sslSession = (SSLSession) session.getAttachment();
			ByteBuffer plaintext = sslSession.read(buf);

			if (plaintext != null && plaintext.hasRemaining()) {
				log.debug("client session {} handshake finished and received cleartext size {}", session.getSessionId(),
						plaintext.remaining());
				if (session.getAttachment() instanceof HTTPConnection) {
					if (next != null) {
						next.decode(plaintext, session);
					}
				} else {
					throw new IllegalStateException("the client http connection has not been created");
				}
			} else {
				log.debug("client ssl session {} is shaking hand", session.getSessionId());
			}
		}
	}

}
