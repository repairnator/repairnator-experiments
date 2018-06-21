/* (c) 2017 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference
// Implementation, v2.0-hudson-3037-ea3
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2007.07.27 at 11:06:51 PM CDT
//
package org.geoserver.wps.ppio.gpx;

import java.util.ArrayList;
import java.util.List;

/**
 * An ordered sequence of points. (for polygons or polylines, e.g.)
 *
 * <p>Java class for ptsegType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ptsegType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pt" type="{http://www.topografix.com/GPX/1/1}ptType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
public class PtsegType {
    protected List<PtType> pt;

    /**
     * Gets the value of the pt property.
     *
     * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the pt property.
     *
     * <p>For example, to add a new item, do as follows:
     *
     * <pre>
     * getPt().add(newItem);
     * </pre>
     *
     * <p>Objects of the following type(s) are allowed in the list {@link PtType }
     */
    public List<PtType> getPt() {
        if (pt == null) {
            pt = new ArrayList<PtType>();
        }

        return this.pt;
    }
}
