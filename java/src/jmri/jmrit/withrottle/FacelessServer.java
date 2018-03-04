package jmri.jmrit.withrottle;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import jmri.InstanceManager;
import jmri.UserPreferencesManager;
import jmri.util.zeroconf.ZeroConfService;
import jmri.util.zeroconf.ZeroConfServiceEvent;
import jmri.util.zeroconf.ZeroConfServiceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copied from UserInterface, but with the UI stuff removed. Sets up to
 * advertise service, and creates a thread for it to run in.
 * <p>
 * listen() has to run in a separate thread.
 *
 * @author Brett Hoffman Copyright (C) 2009, 2010
 */
public class FacelessServer implements DeviceListener, DeviceManager, ZeroConfServiceListener {

    private final static Logger log = LoggerFactory.getLogger(FacelessServer.class);

    UserPreferencesManager userPreferences = InstanceManager.getNullableDefault(UserPreferencesManager.class);

// Server iVars
    int port;
    ZeroConfService service;
    boolean isListen = true;
    ServerSocket socket = null;
    final private ArrayList<DeviceServer> deviceList = new ArrayList<>();

    FacelessServer() {
        createServerThread();
    } // End of constructor

    @Override
    public void listen() {
        int socketPort = InstanceManager.getDefault(WiThrottlePreferences.class).getPort();

        try { //Create socket on available port
            socket = new ServerSocket(socketPort);
        } catch (IOException e1) {
            log.error("New ServerSocket Failed during listen()");
            return;
        }

        port = socket.getLocalPort();
        if (log.isDebugEnabled()) {
            log.debug("WiThrottle listening on TCP port: " + port);
        }

        service = ZeroConfService.create("_withrottle._tcp.local.", port);
        service.addEventListener(this);
        service.publish();

        while (isListen) { //Create DeviceServer threads
            DeviceServer device;
            try {
                log.info("Creating new WiThrottle DeviceServer(socket) on port {}, waiting for incoming connection...", port);
                device = new DeviceServer(socket.accept(), this);  //blocks here until a connection is made

                Thread t = new Thread(device);
                device.addDeviceListener(this);
                log.debug("Starting DeviceListener thread");
                t.start();
            } catch (IOException e3) {
                if (isListen) {
                    log.error("Listen Failed on port {}", port);
                }
                return;
            }

        }

    }

    @Override
    public void notifyDeviceConnected(DeviceServer device) {

        deviceList.add(device);
    }

    @Override
    public void notifyDeviceDisconnected(DeviceServer device) {
        if (deviceList.size() < 1) {
            return;
        }
        if (!deviceList.remove(device)) {
            return;
        }

        device.removeDeviceListener(this);
    }

//    public void notifyDeviceAddressChanged(DeviceServer device){
//    }
    /**
     * Received an UDID, filter out any duplicate.
     * <p>
     * @param device the device to filter for duplicates
     */
    @Override
    public void notifyDeviceInfoChanged(DeviceServer device) {

        //  Filter duplicate connections
        if ((device.getUDID() != null)) {
            for (DeviceServer listDevice : deviceList) {
                if (device != listDevice && device.getUDID().equals(listDevice.getUDID())) {
                    //  If in here, array contains duplicate of a device
                    log.debug("Has duplicate of device, clearing old one.");
                    listDevice.closeThrottles();
                    break;
                }
            }
        }
    }

    @Override
    public String getSelectedRosterGroup() {
//        return rosterGroupSelector.getSelectedRosterGroup();
        return null;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addPropertyChangeListener(String propertyName,
            PropertyChangeListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removePropertyChangeListener(String propertyName,
            PropertyChangeListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyDeviceAddressChanged(DeviceServer device) {
        // TODO Auto-generated method stub

    }

    @Override
    public void serviceQueued(ZeroConfServiceEvent se) {
    }

    @Override
    public void servicePublished(ZeroConfServiceEvent se) {
        try {
            InetAddress addr = se.getDNS().getInetAddress();
            // most addresses are Inet6Address objects,
            if (!addr.isLoopbackAddress()) {
                log.info("Published ZeroConf service for '{}' on {}:{}", se.getService().key(), addr.getHostAddress(), port); // NOI18N
            }
        } catch (NullPointerException ex) {
            log.error("NPE in FacelessServer.servicePublished(): {}", ex.getLocalizedMessage());
        } catch (IOException ex) {
            log.error("IOException in FacelessServer.servicePublished(): {}", ex.getLocalizedMessage());
        }
    }

    @Override
    public void serviceUnpublished(ZeroConfServiceEvent se) {
    }

}
