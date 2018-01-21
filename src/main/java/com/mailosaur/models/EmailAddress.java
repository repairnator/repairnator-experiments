/**
 * Code generated by Microsoft (R) AutoRest Code Generator.
 * Changes may cause incorrect behavior and will be lost if the code is
 * regenerated.
 */

package com.mailosaur.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The EmailAddress model.
 */
public class EmailAddress {
    /**
     * The address property.
     */
    @JsonProperty(value = "address")
    private String address;

    /**
     * The name property.
     */
    @JsonProperty(value = "name")
    private String name;

    /**
     * Get the address value.
     *
     * @return the address value
     */
    public String address() {
        return this.address;
    }

    /**
     * Set the address value.
     *
     * @param address the address value to set
     * @return the EmailAddress object itself.
     */
    public EmailAddress withAddress(String address) {
        this.address = address;
        return this;
    }

    /**
     * Get the name value.
     *
     * @return the name value
     */
    public String name() {
        return this.name;
    }

    /**
     * Set the name value.
     *
     * @param name the name value to set
     * @return the EmailAddress object itself.
     */
    public EmailAddress withName(String name) {
        this.name = name;
        return this;
    }

}
