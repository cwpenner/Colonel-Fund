package com.colonelfund.colonelfund;

/**
 * Created by Penner on 3/6/18.
 */

public class User extends Member {
    public static Member currentUser;

    public User(String userID) {
        super(userID);
    }

    public static Member getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Member newCurrentUser) {
        currentUser = newCurrentUser;
    }

    public static void logout() {
        currentUser = null;
    }
}
