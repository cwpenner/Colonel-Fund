package com.colonelfund.colonelfund;

/**
 * Member list Item Model class.
 */
public class MemberListModel {
    private String userID;
    private String initials;
    private String firstName;
    private String lastName;

    /**
     * Constructor for Initials circle.
     * @param initials
     * @param userID
     */
    public MemberListModel(String initials, String userID, String firstName, String lastName) {
        super();
        this.initials = initials;
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getInitials() {
        return initials;
    }
    public String getUserID() {
        return userID;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getFullName() {
        return firstName + " " + lastName;
    }

}