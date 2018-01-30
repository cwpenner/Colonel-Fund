package com.colonelfund.colonelfund;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;

/**
 * Basic Member Object
 */
public class Member implements Serializable {
    private static final long serialVersionUID = 2628890352033932835L;
    private String userID;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    /**
     * @param userID
     */
    public Member(String userID) {
        super();
        this.userID = userID;
    }
    /**
     * @param userID
     * @param firstName
     * @param lastName
     * @param emailAddress
     * @param phoneNumber
     */
    public Member(String userID, String firstName, String lastName, String emailAddress, String phoneNumber) {
        super();
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }
    /**
     * @return the userID
     */
    public String getUserID() {
        return userID;
    }
    /**
     * @param userID the userID to set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }
    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return a formatted version of the member name. FirstName + LastName with
     * proper capitalization and a space between the names.
     */
    public String getFormattedFullName() {
        return (firstName.substring(0,1).toUpperCase() + firstName.substring(1) +
                " " + lastName.substring(0,1).toUpperCase() + lastName.substring(1));
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }
    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    /**
     * Get a JSONObject representation of Member
     * @return JSONObject
     */
    public JSONObject toJson() throws JSONException {
        JSONObject JsonObj = new JSONObject();
        JsonObj.put("UserID",userID);
        JsonObj.put("FirstName",firstName);
        JsonObj.put("LastName",lastName);
        JsonObj.put("EmailAddress",emailAddress);
        JsonObj.put("PhoneNumber",phoneNumber);
        return JsonObj;
    }
    /**
     * Constructor with JSON Object.
     * @param jsonObject
     */
    public Member(JSONObject jsonObject) throws JSONException {
        this.userID = jsonObject.getString("UserID");
        this.firstName = jsonObject.getString("FirstName");
        this.lastName = jsonObject.getString("LastName");
        this.emailAddress = jsonObject.getString("EmailAddress");
        this.phoneNumber = jsonObject.getString("PhoneNumber");
    }

}
