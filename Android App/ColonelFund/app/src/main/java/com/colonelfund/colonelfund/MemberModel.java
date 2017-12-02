package com.colonelfund.colonelfund;

/**
 * Created by JWilk on 12/2/2017.
 */

public class MemberModel {

    private String member_id;
    private String initials;

    public MemberModel(String initials, String member_id) {
        super();
        this.initials = initials;
        this.member_id = member_id;
    }
    public String getInitials() {
        return initials;
    }
    public String getMemberID() {
        return member_id;
    }

}