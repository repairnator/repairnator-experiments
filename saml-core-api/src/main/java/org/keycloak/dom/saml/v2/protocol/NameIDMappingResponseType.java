/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.keycloak.dom.saml.v2.protocol;

import org.keycloak.dom.saml.v2.assertion.EncryptedElementType;
import org.keycloak.dom.saml.v2.assertion.NameIDType;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for NameIDMappingResponseType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="NameIDMappingResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:SAML:2.0:protocol}StatusResponseType">
 *       &lt;choice>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}NameID"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}EncryptedID"/>
 *       &lt;/choice>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
public class NameIDMappingResponseType extends StatusResponseType {

    protected NameIDType nameID;

    protected EncryptedElementType encryptedID;

    public NameIDMappingResponseType(String id, XMLGregorianCalendar issueInstant) {
        super(id, issueInstant);
    }

    /**
     * Gets the value of the nameID property.
     *
     * @return possible object is {@link NameIDType }
     */
    public NameIDType getNameID() {
        return nameID;
    }

    /**
     * Sets the value of the nameID property.
     *
     * @param value allowed object is {@link NameIDType }
     */
    public void setNameID(NameIDType value) {
        this.nameID = value;
    }

    /**
     * Gets the value of the encryptedID property.
     *
     * @return possible object is {@link EncryptedElementType }
     */
    public EncryptedElementType getEncryptedID() {
        return encryptedID;
    }

    /**
     * Sets the value of the encryptedID property.
     *
     * @param value allowed object is {@link EncryptedElementType }
     */
    public void setEncryptedID(EncryptedElementType value) {
        this.encryptedID = value;
    }
}