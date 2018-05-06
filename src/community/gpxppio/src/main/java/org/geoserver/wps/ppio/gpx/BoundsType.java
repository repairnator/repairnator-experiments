/* (c) 2017 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-hudson-3037-ea3 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.07.27 at 11:06:51 PM CDT 
//
package org.geoserver.wps.ppio.gpx;

/**
 * 
 * Two lat/lon pairs defining the extent of an element.
 * 
 * 
 * <p>
 * Java class for boundsType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="boundsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="maxlat" use="required" type="{http://www.topografix.com/GPX/1/1}latitudeType" />
 *       &lt;attribute name="maxlon" use="required" type="{http://www.topografix.com/GPX/1/1}longitudeType" />
 *       &lt;attribute name="minlat" use="required" type="{http://www.topografix.com/GPX/1/1}latitudeType" />
 *       &lt;attribute name="minlon" use="required" type="{http://www.topografix.com/GPX/1/1}longitudeType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
public class BoundsType {
    protected double maxlat;

    protected double maxlon;

    protected double minlat;

    protected double minlon;

    /**
     * Gets the value of the maxlat property.
     * 
     */
    public double getMaxlat() {
        return maxlat;
    }

    /**
     * Sets the value of the maxlat property.
     * 
     */
    public void setMaxlat(double value) {
        this.maxlat = value;
    }

    /**
     * Gets the value of the maxlon property.
     * 
     */
    public double getMaxlon() {
        return maxlon;
    }

    /**
     * Sets the value of the maxlon property.
     * 
     */
    public void setMaxlon(double value) {
        this.maxlon = value;
    }

    /**
     * Gets the value of the minlat property.
     * 
     */
    public double getMinlat() {
        return minlat;
    }

    /**
     * Sets the value of the minlat property.
     * 
     */
    public void setMinlat(double value) {
        this.minlat = value;
    }

    /**
     * Gets the value of the minlon property.
     * 
     */
    public double getMinlon() {
        return minlon;
    }

    /**
     * Sets the value of the minlon property.
     * 
     */
    public void setMinlon(double value) {
        this.minlon = value;
    }
}
