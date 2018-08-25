package jmri.jmrix.dccpp;

/**
 * DCCppInterface defines the general connection to a DCC++ layout.
 * <P>
 * Use this interface to send messages to a DCC++ layout. Classes implementing
 * the DCCppListener interface can register here to receive incoming DCC++
 * messages as events.
 * <P>
 * The jmri.jrmix.dccpp.DCCppTrafficControler provides the first implementation of
 * this interface.
 * <P>
 * How do you locate an implemenation of this interface? That's an interesting
 * question. This is inherently DCC++ specific, so it would be inappropriate to
 * put it in the jmri.InterfaceManager. And Java interfaces can't have static
 * members, so we can't provide an implementation() member. For now, we use a
 * static implementation member in the DCCppTrafficController implementation to
 * locate _any_ implementation; this clearly needs to be improved.
 * <P>
 * DCCppListener implementations registering for traffic updates cannot assume
 * that messages will be returned in any particular thread. See the DCCppListener
 * doc for more background.
 *
 * @author Bob Jacobsen Copyright (C) 2001, 2002
 * @author Mark Underwood Copyright (C) 2015
 * @see jmri.jmrix.dccpp.DCCppListener
 * @see jmri.jmrix.dccpp.DCCppTrafficController
 *
 * Based on jmri.jmrix.lenz.XNetInterface
 */
public interface DCCppInterface {

    /*
     * Request a message be sent to the attached DCC++. Return is immediate,
     * with the message being queued for eventual sending.  If you're interested
     * in a reply, you need to register a DCCppListener object to watch the
     * message stream. When sending, you specify (in 2nd parameter) who
     * you are so you're not redundantly notified of this message.
     */
    public void sendDCCppMessage(DCCppMessage msg, DCCppListener replyTo);

    /**
     * Request notification of things happening on the DCC++.
     * <P>
     * The same listener can register multiple times with different masks.
     * (Multiple registrations with a single mask value are equivalent to a
     * single registration) Mask values are defined as class constants. Note
     * that these are bit masks, and should be OR'd, not added, if multiple
     * values are desired.
     * <P>
     * The event notification contains the received message as source, not this
     * object, so that we can notify of an incoming message to multiple places
     * and then move on.
     *
     * @param mask The OR of the key values of messages to be reported (to
     *             reduce traffic, provide for listeners interested in different
     *             things)
     *
     * @param l    Object to be notified of new messages as they arrive.
     *
     */
    void addDCCppListener(int mask, DCCppListener l);

    /*
     * Stop notification of things happening on the XNet. Note that mask and XNetListener
     * must match a previous request exactly.
     */
    void removeDCCppListener(int mask, DCCppListener listener);

    /*
     * Check whether an implementation is operational. True indicates OK.
     */
    public boolean status();

    /**
     * Mask value to request notification of all incoming messages
     */
    public static final int ALL = ~0;

    /**
     * Mask value to request notification of communications related messages
     * generated by the computer interface
     */
    public static final int COMMINFO = 1;

    /**
     * Mask value to request notification of Command Station informational
     * messages This includes all broadcast messages, except for the feedback
     * broadcast and all programming messages
     */
    public static final int CS_INFO = 2;

    /**
     * Mask value to request notification of messages associated with
     * programming
     */
    public static final int PROGRAMMING = 4;

    /**
     * Mask value to request notification of XPressNet FeedBack (i.e. sensor)
     * related messages
     */
    public static final int FEEDBACK = 8;

    /**
     * Mask value to request notification of messages associated with throttle
     * status
     *
     */
    public static final int THROTTLE = 16;

    /**
     * Mask value to request notification of messages associated with consists
     *
     */
    public static final int CONSIST = 32;

    /**
     * Mask value to request notification of messages associated with consists
     *
     */
    public static final int INTERFACE = 64;

}



