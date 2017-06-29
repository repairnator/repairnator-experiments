//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference
// Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2014.10.23 at 08:50:01 AM PDT
//


package org.openpnp.model.eagle.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "gate")
public class Gate {

    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String name;
    @XmlAttribute(name = "symbol", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String symbol;
    @XmlAttribute(name = "x", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String x;
    @XmlAttribute(name = "y", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String y;
    @XmlAttribute(name = "addlevel")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String addlevel;
    @XmlAttribute(name = "swaplevel")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String swaplevel;

    /**
     * Gets the value of the name property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the symbol property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Sets the value of the symbol property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setSymbol(String value) {
        this.symbol = value;
    }

    /**
     * Gets the value of the x property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getX() {
        return x;
    }

    /**
     * Sets the value of the x property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setX(String value) {
        this.x = value;
    }

    /**
     * Gets the value of the y property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getY() {
        return y;
    }

    /**
     * Sets the value of the y property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setY(String value) {
        this.y = value;
    }

    /**
     * Gets the value of the addlevel property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAddlevel() {
        if (addlevel == null) {
            return "next";
        }
        else {
            return addlevel;
        }
    }

    /**
     * Sets the value of the addlevel property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setAddlevel(String value) {
        this.addlevel = value;
    }

    /**
     * Gets the value of the swaplevel property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSwaplevel() {
        if (swaplevel == null) {
            return "0";
        }
        else {
            return swaplevel;
        }
    }

    /**
     * Sets the value of the swaplevel property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setSwaplevel(String value) {
        this.swaplevel = value;
    }

}
