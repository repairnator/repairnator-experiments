//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.09.20 at 07:15:53 PM MESZ 
//


package org.matsim.jaxb.lanedefinitions11;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for lanesToLinkAssignmentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lanesToLinkAssignmentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lane" type="{http://www.matsim.org/files/dtd}laneType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="linkIdRef" type="{http://www.matsim.org/files/dtd}matsimIdType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lanesToLinkAssignmentType", propOrder = {
    "lane"
})
public class XMLLanesToLinkAssignmentType {

    @XmlElement(required = true)
    protected List<XMLLaneType> lane;
    @XmlAttribute
    protected String linkIdRef;

    /**
     * Gets the value of the lane property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lane property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLane().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLLaneType }
     * 
     * 
     */
    public List<XMLLaneType> getLane() {
        if (lane == null) {
            lane = new ArrayList<XMLLaneType>();
        }
        return this.lane;
    }

    /**
     * Gets the value of the linkIdRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkIdRef() {
        return linkIdRef;
    }

    /**
     * Sets the value of the linkIdRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkIdRef(String value) {
        this.linkIdRef = value;
    }

}
