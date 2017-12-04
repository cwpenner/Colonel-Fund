package com.colonelfund.colonelfund;

/**
 * Member list Item Model class.
 */
public class MemberListModel {
    private String member_id;
    private String initials;

    /**
     * Constructor for Initials circle.
     * @param initials
     * @param member_id
     */
    public MemberListModel(String initials, String member_id) {
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