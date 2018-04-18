package com.colonelfund.colonelfund;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Basic Address Object that holds all the information for an address
 */
public class Address implements Serializable {
    private static final long serialVersionUID = 5189264305784235789L;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;

    /**
     * Empty Address constructor
     *
     */
    public Address() {
        this.addressLine1 = "";
        this.addressLine2 = "";
        this.city = "";
        this.state = "";
        this.zipCode = "";
    }

    /**
     * Full constructor for an Address
     *
     * @param addressLine1 of an address
     * @param addressLine2 of an address
     * @param city of an address
     * @param state of an address
     * @param zipCode of an address
     */
    public Address(String addressLine1, String addressLine2, String city, String state, String zipCode) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    /**
     *
     * @return the first line of an address
     */
    public String getAddressLine1() {
        return addressLine1;
    }

    /**
     *
     * @param addressLine1 the first line of an address to set
     */
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    /**
     *
     * @return the second line of an address
     */
    public String getAddressLine2() {
        return addressLine2;
    }

    /**
     *
     * @param addressLine2 the second line of an address to set
     */
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    /**
     *
     * @return the city of an address
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city the city of an address to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     *
     * @return the state of an address
     */
    public String getState() {
        return state;
    }

    /**
     *
     * @param state the state of an address to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     *
     * @return the zip code of an address
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     *
     * @param zipCode the zip code of an address to set
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String toString() {
        return addressLine1 + "\n" + addressLine2 + "\n" + city + " " + state + " " + zipCode;
    }

    /**
     * @return an address json
     * @throws JSONException for address json creation
     */
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("addressLine1", addressLine1);
        jsonObj.put("addressLine2", addressLine2);
        jsonObj.put("city", city);
        jsonObj.put("state", state);
        jsonObj.put("zipCode", zipCode);
        return jsonObj;
    }

    /**
     * Constructor with JSON Object
     *
     * @param jsonObject of an address
     */
    public Address(JSONObject jsonObject) throws JSONException {
        this.addressLine1 = jsonObject.getString("addressLine1");
        this.addressLine2 = jsonObject.getString("addressLine2");
        this.city = jsonObject.getString("city");
        this.state = jsonObject.getString("state");
        this.zipCode = jsonObject.getString("zipCode");
    }
}
