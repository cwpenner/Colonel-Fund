package com.colonelfund.colonelfund;

/**
 * Member list Item Model class.
 */
public class MemberListModel {
    private String userID;
    private String initials;
    private String firstName;
    private String lastName;
    private String associatedEmail;

    /**
     * Constructor for Initials circle.
     *
     * @param initials user initials
     * @param userID user id
     */
    public MemberListModel(String initials, String userID, String firstName, String lastName, String associatedEmail) {
        super();
        this.initials = initials;
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.associatedEmail = associatedEmail;
    }

    /**
     * @return String for initials
     */
    public String getInitials() {
        return initials;
    }

    /**
     * @return String for user id
     */
    public String getUserID() {
        return userID;
    }

    /**
     * @return string for first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return string for last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return string for full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * @return string for email
     */
    public String getEmail() {
        return associatedEmail;
    }
}