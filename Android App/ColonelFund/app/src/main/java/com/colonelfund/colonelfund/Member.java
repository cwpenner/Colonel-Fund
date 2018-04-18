package com.colonelfund.colonelfund;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Basic Member Object that holds all the information for a member.
 */
public class Member implements Serializable {
    private static final long serialVersionUID = 2628890352033932835L;
    private String userID;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;

    private String username;
    private String profilePicURL;
    private String facebookID;
    private String googleID;
    private String firebaseID;

    /**
     * Full constructor for member.
     *
     * @param userID of a member.
     * @param firstName of a member.
     * @param lastName of a member.
     * @param username of a member.
     * @param emailAddress of a member.
     * @param phoneNumber of a member.
     * @param profilePicURL of a member.
     * @param facebookID of a member.
     * @param googleID of a member.
     */
    public Member(String userID, String firstName, String lastName, String emailAddress, String phoneNumber, String username, String profilePicURL, String facebookID, String googleID, String firebaseID) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.profilePicURL = profilePicURL;
        this.facebookID = facebookID;
        this.googleID = googleID;
        this.firebaseID = firebaseID;
        if (userID == "") {
            this.userID = "01"; //TODO: change once we have usernames figured out
        }

        if (username.equals("")) {
            makeUserName();
        }
    }

    /**
     * Constructor with just user id
     *
     * @param userID of a member.
     */
    public Member(String userID) {
        super();
        this.userID = userID;

        if (this.username.equals("")) {
            makeUserName();
        }
    }

    /**
     *
     * @return the firebaseID
     */
    public String getFirebaseID() {
        return firebaseID;
    }

    /**
     *
     * @param firebaseID the firebaseID to set
     */
    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
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
        return (firstName.substring(0, 1).toUpperCase() + firstName.substring(1) +
                " " + lastName.substring(0, 1).toUpperCase() + lastName.substring(1));
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
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
     * @return the profilePicURL
     */
    public String getProfilePicURL() {
        return profilePicURL;
    }

    /**
     * @param profilePicURL the profilePicURL to set
     */
    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    /**
     * @return the facebookID
     */
    public String getFacebookID() {
        return facebookID;
    }

    /**
     * @param facebookID the facebookID to set
     */
    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    /**
     * @return the googleID
     */
    public String getGoogleID() {
        return googleID;
    }

    /**
     * @param googleID the googleID to set
     */
    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    /**
     * Creates a user name from first/last
     */
    private void makeUserName() {
        this.username = this.firstName.toLowerCase() + this.lastName.toLowerCase();
    }

    /**
     * Get a JSONObject representation of Member
     *
     * @return JSONObject
     */
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("userID", userID);
        jsonObj.put("firstName", firstName);
        jsonObj.put("lastName", lastName);
        jsonObj.put("emailAddress", emailAddress);
        jsonObj.put("phoneNumber", phoneNumber);

        jsonObj.put("username", username);
        jsonObj.put("profilePicURL", profilePicURL);
        jsonObj.put("facebookID", facebookID);
        jsonObj.put("googleID", googleID);
        jsonObj.put("firebaseID", firebaseID);
        return jsonObj;
    }

    /**
     * Constructor with JSON Object.
     *
     * @param jsonObject
     */
    public Member(JSONObject jsonObject) throws JSONException {
        this.userID = jsonObject.getString("userID");
        this.firstName = jsonObject.getString("firstName");
        this.lastName = jsonObject.getString("lastName");
        this.emailAddress = jsonObject.getString("emailAddress");
        this.phoneNumber = jsonObject.getString("phoneNumber");
        this.username = jsonObject.getString("username");
        this.profilePicURL = jsonObject.getString("profilePicURL");
        this.facebookID = jsonObject.getString("facebookID");
        this.googleID = jsonObject.getString("googleID");
        this.firebaseID = jsonObject.getString("firebaseID");
    }

}
