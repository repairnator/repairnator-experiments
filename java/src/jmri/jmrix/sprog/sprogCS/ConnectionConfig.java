package jmri.jmrix.sprog.sprogCS;

import jmri.util.SystemType;

/**
 * Definition of objects to handle configuring a layout connection via an SPROG
 * SerialDriverAdapter object.
 *
 * @author Andrew Crosland Copyright (C) 2006
  */
public class ConnectionConfig extends jmri.jmrix.AbstractSerialConnectionConfig {

    /**
     * Ctor for an object being created during load process; Swing init is
     * deferred.
     */
    public ConnectionConfig(jmri.jmrix.SerialPortAdapter p) {
        super(p);
    }

    /**
     * Ctor for a functional Swing object with no prexisting adapter
     */
    public ConnectionConfig() {
        super();
    }

    @Override
    public String name() {
        return Bundle.getMessage("SprogCSTitle");
    }

    @Override
    public String getManufacturer() {
        return adapter.getManufacturer();
    }

    @Override
    public void setManufacturer(String manu) {
        adapter.setManufacturer(manu);
    }
    /*@Override
     protected Vector<String> getPortFriendlyNames() {
     System.out.println("Port names called");
     Vector<String> portNameVector = new Vector<String>();
     if(System.getProperty("os.name").toLowerCase().contains("windows")){
     portNameVector.add("SPROG");
     }
     System.out.println("Port names called" + portNameVector);
     return portNameVector;
     }*/

    @Override
    protected String[] getPortFriendlyNames() {
        if (SystemType.isWindows()) {
            return new String[]{"SPROG"};
        }
        return new String[]{};
    }

    @Override
    protected void setInstance() {
        if(adapter == null ) {
           adapter = new SprogCSSerialDriverAdapter();
        } 
    }
}
