/*******************************************************************************
 * Copyright (c) 2009, 2016 IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution. 
 *
 * The Eclipse Public License is available at 
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 *   http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 * Contributors:
 *   Ian Craggs - MQTT 3.1.1 support
 *   Ian Craggs - fix bug 469527
 *   James Sutton - Automatic Reconnect & Offline Buffering
 */
package org.eclipse.paho.mqttv5.client.internal;

import org.eclipse.paho.mqttv5.client.MqttActionListener;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttCallbackExtended;
import org.eclipse.paho.mqttv5.client.MqttClientPersistence;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.MqttToken;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttPersistenceException;

/**
 * <p>
 * This class handles the connection of the AsyncClient to one of the available
 * URLs.
 * </p>
 * <p>
 * The URLs are supplied as either the singleton when the client is created, or
 * as a list in the connect options.
 * </p>
 * <p>
 * This class uses its own onSuccess and onFailure callbacks in preference to
 * the user supplied callbacks.
 * </p>
 * <p>
 * An attempt is made to connect to each URL in the list until either a
 * connection attempt succeeds or all the URLs have been tried
 * </p>
 * <p>
 * If a connection succeeds then the users token is notified and the users
 * onSuccess callback is called.
 * </p>
 * <p>
 * If a connection fails then another URL in the list is attempted, otherwise
 * the users token is notified and the users onFailure callback is called
 * </p>
 */
public class ConnectActionListener implements MqttActionListener {

	private MqttClientPersistence persistence;
	private MqttAsyncClient client;
	private ClientComms comms;
	private MqttConnectionOptions options;
	private MqttToken userToken;
	private Object userContext;
	private MqttActionListener userCallback;
	private MqttCallbackExtended mqttCallbackExtended;
	private boolean reconnect;

	/**
	 * @param persistence
	 *            The {@link MqttClientPersistence} layer
	 * @param client
	 *            the {@link MqttAsyncClient}
	 * @param comms
	 *            {@link ClientComms}
	 * @param options
	 *            the {@link MqttConnectionOptions}
	 * @param userToken
	 *            the {@link MqttToken}
	 * @param userContext
	 *            the User Context Object
	 * @param userCallback
	 *            the {@link MqttActionListener} as the callback for the user
	 * @param reconnect
	 *            If true, this is a reconnect attempt
	 */
	public ConnectActionListener(MqttAsyncClient client, MqttClientPersistence persistence, ClientComms comms,
			MqttConnectionOptions options, MqttToken userToken, Object userContext, MqttActionListener userCallback,
			boolean reconnect) {
		this.persistence = persistence;
		this.client = client;
		this.comms = comms;
		this.options = options;
		this.userToken = userToken;
		this.userContext = userContext;
		this.userCallback = userCallback;
		this.reconnect = reconnect;
		
	}

	/**
	 * If the connect succeeded then call the users onSuccess callback
	 * 
	 * @param token
	 *            the {@link IMqttToken} from the successful connection
	 */
	public void onSuccess(IMqttToken token) {
		userToken.internalTok.markComplete(token.getResponse(), null);
		userToken.internalTok.notifyComplete();
		userToken.internalTok.setClient(this.client); // fix bug 469527 - maybe should be set elsewhere?

		// Set properties imposed on us by the Server
		comms.setReceiveMaximum(((MqttToken) token).getRecieveMaximum());
		comms.setMaximumQoS(((MqttToken) token).getMaximumQoS());
		comms.setRetainAvailable(((MqttToken) token).isRetainAvailable());
		comms.setMaximumPacketSize(((MqttToken) token).getMaximumPacketSize());
		comms.setTopicAliasMaximum(((MqttToken) token).getTopicAliasMaximum());
		comms.setWildcardSubscriptionsAvailable(((MqttToken) token).isWildcardSubscriptionAvailable());
		comms.setSubscriptionIdentifiersAvailable(((MqttToken) token).isSubscriptionIdentifiersAvailable());
		comms.setSharedSubscriptionsAvailable(((MqttToken) token).isSharedSubscriptionAvailable());

		if (reconnect) {
			comms.notifyReconnect();
		}

		if (userCallback != null) {
			userToken.setUserContext(userContext);
			userCallback.onSuccess(userToken);
		}

		if (mqttCallbackExtended != null) {
			String serverURI = comms.getNetworkModules()[comms.getNetworkModuleIndex()].getServerURI();
			mqttCallbackExtended.connectComplete(reconnect, serverURI);
		}

	}

	/**
	 * The connect failed, so try the next URI on the list. If there are no more
	 * URIs, then fail the overall connect.
	 * 
	 * @param token
	 *            the {@link IMqttToken} from the failed connection attempt
	 * @param exception
	 *            the {@link Throwable} exception from the failed connection attempt
	 */
	public void onFailure(IMqttToken token, Throwable exception) {

		int numberOfURIs = comms.getNetworkModules().length;
		int index = comms.getNetworkModuleIndex();

		if ((index + 1) < numberOfURIs) {

			comms.setNetworkModuleIndex(index + 1);

			try {
				connect();
			} catch (MqttPersistenceException e) {
				onFailure(token, e); // try the next URI in the list
			}
		} else {

			MqttException ex;
			if (exception instanceof MqttException) {
				ex = (MqttException) exception;
			} else {
				ex = new MqttException(exception);
			}
			userToken.internalTok.markComplete(null, ex);
			userToken.internalTok.notifyComplete();
			userToken.internalTok.setClient(this.client); // fix bug 469527 - maybe should be set elsewhere?

			if (userCallback != null) {
				userToken.setUserContext(userContext);
				userCallback.onFailure(userToken, exception);
			}
		}
	}

	/**
	 * Start the connect processing
	 * 
	 * @throws MqttPersistenceException
	 *             if an error is thrown whilst setting up persistence
	 */
	public void connect() throws MqttPersistenceException {
		MqttToken token = new MqttToken(client.getClientId());
		token.setActionCallback(this);
		token.setUserContext(this);

		persistence.open(client.getClientId());

		if (options.isCleanSession()) {
			persistence.clear();
		}

		try {
			comms.connect(options, token);
		} catch (MqttException e) {
			onFailure(token, e);
		}
	}

	/**
	 * Set the MqttCallbackExtened callback to receive connectComplete callbacks
	 * 
	 * @param mqttCallbackExtended
	 *            the {@link MqttCallbackExtended} to be called when the connection
	 *            completes
	 */
	public void setMqttCallbackExtended(MqttCallbackExtended mqttCallbackExtended) {
		this.mqttCallbackExtended = mqttCallbackExtended;
	}

}
