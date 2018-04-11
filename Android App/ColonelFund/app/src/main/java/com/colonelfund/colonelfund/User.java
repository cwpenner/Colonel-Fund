package com.colonelfund.colonelfund;

/**
 * User class to handle current user.
 */
public class User extends Member {
    public static Member currentUser;

    /**
     * Constructor of user with user id.
     *
     * @param userID of a member
     */
    public User(String userID) {
        super(userID);
    }

    /**
     * Gets the current user.
     *
     * @return the current user.
     */
    public static Member getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current user.
     *
     * @param newCurrentUser sets the member as current user.
     */
    public static void setCurrentUser(Member newCurrentUser) {
        currentUser = newCurrentUser;
    }

    /**
     * Logs out the current user.
     */
    public static void logout() {
        currentUser = null;
    }
}
