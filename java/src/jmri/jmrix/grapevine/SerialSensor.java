package jmri.jmrix.grapevine;

import jmri.implementation.AbstractSensor;

/**
 * Implement AbstractSensor for Grapevine. Really doesn't do much, because the
 * abstract class and the SerialSensorManager do all the work in a node-based
 * system.
 *
 * @author Bob Jacobsen Copyright (C) 2003, 2006, 2007, 2008
 */
public class SerialSensor extends AbstractSensor {

    public SerialSensor(String systemName, GrapevineSystemConnectionMemo _memo) {
        super(systemName);
        _knownState = UNKNOWN;
    }

    public SerialSensor(String systemName, String userName, GrapevineSystemConnectionMemo _memo) {
        super(systemName, userName);
        _knownState = UNKNOWN;
    }

    /**
     * Request an update on status.
     * <p>
     * Since status is continually being updated, this isn't active now.
     * Eventually, we may want to have this move the related AIU to the top of
     * the polling queue.
     */
    @Override
    public void requestUpdateFromLayout() {
    }

}
