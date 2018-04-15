package org.hl7.fhir.r4.model.api;



/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * Base interface for ID datatype. 
 * 
 * <p>
 * <b>Concrete Implementations:</b> This interface is often returned and/or accepted by methods in HAPI's API
 * where either {@link ca.uhn.fhir.model.primitive.IdDt} (the HAPI structure ID type) or 
 * <code>org.hl7.fhir.instance.model.IdType</code> (the RI structure ID type) will be used, depending on 
 * which version of the strctures your application is using.   
 * </p>
 */
public interface IIdType {

	void applyTo(IBaseResource theResource);

	/**
	 * Returns the server base URL if this ID contains one. For example, the base URL is
	 * the 'http://example.com/fhir' in the following ID: <code>http://example.com/fhir/Patient/123/_history/55</code>
	 */
	String getBaseUrl();

	/**
	 * Returns only the logical ID part of this ID. For example, given the ID
	 * "http://example,.com/fhir/Patient/123/_history/456", this method would
	 * return "123".
	 */
	String getIdPart();

	/**
	 * Returns the ID part of this ID (e.g. in the ID http://example.com/Patient/123/_history/456 this would be the
	 * part "123") parsed as a {@link Long}.
	 * 
	 * @throws NumberFormatException If the value can't be parsed as a long
	 */
	Long getIdPartAsLong();

	String getResourceType();

	/**
	 * Returns the value of this ID. Note that this value may be a fully qualified URL, a relative/partial URL, or a simple ID. Use {@link #getIdPart()} to get just the ID portion.
	 * 
	 * @see #getIdPart()
	 */
	String getValue();

	String getVersionIdPart();

	/**
	 * Returns the version ID part of this ID (e.g. in the ID http://example.com/Patient/123/_history/456 this would be the
	 * part "456") parsed as a {@link Long}.
	 * 
	 * @throws NumberFormatException If the value can't be parsed as a long
	 */
	Long getVersionIdPartAsLong();

	boolean hasBaseUrl();

	/**
	 * Returns <code>true</code> if this ID contains an actual ID part. For example, the ID part is
	 * the '123' in the following ID: <code>http://example.com/fhir/Patient/123/_history/55</code>
	 */
	boolean hasIdPart();

	boolean hasResourceType();

	boolean hasVersionIdPart();

	/**
	 * Returns <code>true</code> if this ID contains an absolute URL (in other words, a URL starting with "http://" or "https://"
	 */
	boolean isAbsolute();

	boolean isEmpty();

	/**
	 * Returns <code>true</code> if the {@link #getIdPart() ID part of this object} is valid according to the FHIR rules for valid IDs. 
	 * <p>
	 * The FHIR specification states:
	 * <code>Any combination of upper or lower case ASCII letters ('A'..'Z', and 'a'..'z', numerals ('0'..'9'), '-' and '.', with a length limit of 64 characters. (This might be an integer, an un-prefixed OID, UUID or any other identifier pattern that meets these constraints.) regex: [A-Za-z0-9\-\.]{1,64}</code>
	 * </p>
	 */
	boolean isIdPartValid();

	/**
	 * Returns <code>true</code> if the {@link #getIdPart() ID part of this object} contains
	 * only numbers 
	 */
	boolean isIdPartValidLong();

	/**
	 * Returns <code>true</code> if the ID is a local reference (in other words, it begins with the '#' character)
	 */
	boolean isLocal();

	/**
	 * Returns <code>true</code> if the {@link #getVersionIdPart() version ID part of this object} contains
	 * only numbers 
	 */
	boolean isVersionIdPartValidLong();

	IIdType setValue(String theString);

	IIdType toUnqualified();

	IIdType toUnqualifiedVersionless();

	IIdType toVersionless();

	IIdType withResourceType(String theResName);
	
	IIdType withServerBase(String theServerBase, String theResourceName);

	IIdType withVersion(String theVersion);

}
