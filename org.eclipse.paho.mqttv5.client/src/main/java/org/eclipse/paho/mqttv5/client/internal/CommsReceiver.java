/*******************************************************************************
 * Copyright (c) 2009, 2014 IBM Corp.
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
 *    Dave Locke - initial API and implementation and/or initial documentation
 */
package org.eclipse.paho.mqttv5.client.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

import org.eclipse.paho.mqttv5.client.MqttClientException;
import org.eclipse.paho.mqttv5.client.MqttToken;
import org.eclipse.paho.mqttv5.client.logging.Logger;
import org.eclipse.paho.mqttv5.client.logging.LoggerFactory;
import org.eclipse.paho.mqttv5.client.oldwire.MqttInputStream;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.packet.MqttAck;
import org.eclipse.paho.mqttv5.common.packet.MqttDisconnect;
import org.eclipse.paho.mqttv5.common.packet.MqttPubAck;
import org.eclipse.paho.mqttv5.common.packet.MqttPubComp;
import org.eclipse.paho.mqttv5.common.packet.MqttPubRec;
import org.eclipse.paho.mqttv5.common.packet.MqttWireMessage;

/**
 * Receives MQTT packets from the server.
 */
public class CommsReceiver implements Runnable {
	private static final String CLASS_NAME = CommsReceiver.class.getName();
	private static final Logger log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);

	private boolean running = false;
	private Object lifecycle = new Object();
	private ClientState clientState = null;
	private ClientComms clientComms = null;
	private MqttInputStream in;
	private CommsTokenStore tokenStore = null;
	private Thread recThread = null;
	private volatile boolean receiving;
	private final Semaphore runningSemaphore = new Semaphore(1);
	private String threadName;
	private Future<?> receiverFuture;

	public CommsReceiver(ClientComms clientComms, ClientState clientState, CommsTokenStore tokenStore, InputStream in) {
		this.in = new MqttInputStream(clientState, in);
		this.clientComms = clientComms;
		this.clientState = clientState;
		this.tokenStore = tokenStore;
		log.setResourceName(clientComms.getClient().getClientId());
	}

	/**
	 * Starts up the Receiver's thread.
	 * 
	 * @param threadName
	 *            the thread name.
	 * @param executorService
	 *            used to execute the thread
	 */
	public void start(String threadName, ExecutorService executorService) {
		this.threadName = threadName;
		final String methodName = "start";
		// @TRACE 855=starting
		log.fine(CLASS_NAME, methodName, "855");
		synchronized (lifecycle) {
			if (!running) {
				running = true;
				receiverFuture = executorService.submit(this);
			}
		}
	}

	/**
	 * Stops the Receiver's thread. This call will block.
	 */
	public void stop() {
		final String methodName = "stop";
		synchronized (lifecycle) {
			if (receiverFuture != null) {
				receiverFuture.cancel(true);
			}
			// @TRACE 850=stopping
			log.fine(CLASS_NAME, methodName, "850");
			if (running) {
				running = false;
				receiving = false;
				if (!Thread.currentThread().equals(recThread)) {
					try {
						// Wait for the thread to finish.
						runningSemaphore.acquire();
					} catch (InterruptedException ex) {
					} finally {
						runningSemaphore.release();
					}
				}
			}
		}
		recThread = null;
		// @TRACE 851=stopped
		log.fine(CLASS_NAME, methodName, "851");
	}

	/**
	 * Run loop to receive messages from the server.
	 */
	public void run() {
		recThread = Thread.currentThread();
		recThread.setName(threadName);
		final String methodName = "run";
		MqttToken token = null;

		try {
			runningSemaphore.acquire();
		} catch (InterruptedException e) {
			running = false;
			return;
		}

		while (running && (in != null)) {
			try {
				// @TRACE 852=network read message
				log.fine(CLASS_NAME, methodName, "852");
				receiving = in.available() > 0;
				MqttWireMessage message = in.readMqttWireMessage();
				receiving = false;

				// instanceof checks if message is null
				if (message instanceof MqttAck) {
					token = tokenStore.getToken(message);
					if (token != null) {
						synchronized (token) {
							// Ensure the notify processing is done under a lock on the token
							// This ensures that the send processing can complete before the
							// receive processing starts! ( request and ack and ack processing
							// can occur before request processing is complete if not!
							clientState.notifyReceivedAck((MqttAck) message);
						}
					} else if (message instanceof MqttPubRec || message instanceof MqttPubComp
							|| message instanceof MqttPubAck) {
						// This is an ack for a message we no longer have a ticket for.
						// This probably means we already received this message and it's being send
						// again
						// because of timeouts, crashes, disconnects, restarts etc.
						// It should be safe to ignore these unexpected messages.
						log.fine(CLASS_NAME, methodName, "857");
					} else {
						// It its an ack and there is no token then something is not right.
						// An ack should always have a token assoicated with it.
						throw new MqttException(MqttClientException.REASON_CODE_UNEXPECTED_ERROR);
					}
				} else if (message != null && message instanceof MqttDisconnect) {
					// This is a Disconnect Message
						clientComms.shutdownConnection(null, new MqttException(MqttClientException.REASON_CODE_SERVER_DISCONNECTED), (MqttDisconnect) message);
				} else {
					if (message != null) {
						// A new message has arrived
						clientState.notifyReceivedMsg(message);
					}
				}
			} catch (MqttException ex) {
				// @TRACE 856=Stopping, MQttException
				log.fine(CLASS_NAME, methodName, "856", null, ex);
				running = false;
				// Token maybe null but that is handled in shutdown
				clientComms.shutdownConnection(token, ex, null);
			} catch (IOException ioe) {
				// @TRACE 853=Stopping due to IOException
				log.fine(CLASS_NAME, methodName, "853");

				running = false;
				// An EOFException could be raised if the broker processes the
				// DISCONNECT and ends the socket before we complete. As such,
				// only shutdown the connection if we're not already shutting down.
				if (!clientComms.isDisconnecting()) {
					clientComms.shutdownConnection(token,
							new MqttException(MqttClientException.REASON_CODE_CONNECTION_LOST, ioe), null);
				}
			} finally {
				receiving = false;
				runningSemaphore.release();
			}
		}

		// @TRACE 854=<
		log.fine(CLASS_NAME, methodName, "854");
	}

	public boolean isRunning() {
		return running;
	}

	/**
	 * Returns the receiving state.
	 *
	 * @return true if the receiver is receiving data, false otherwise.
	 */
	public boolean isReceiving() {
		return receiving;
	}
}
