package com.colonelfund.colonelfund;

/**
 * Member list Item Model class.
 */
public class MemberListModel {
    private String initials;
    private String member_id;
    private String member_name;

    /**
     * Constructor for Initials circle.
     * @param initials
     * @param member_id
     * @param member_name
     */
    public MemberListModel(String initials, String member_id, String member_name) {
        super();
        this.initials = initials;
        this.member_id = member_id;
        this.member_name = member_name;
    }

    public String getInitials() {
        return initials;
    }
    public String getMemberID() {
        return member_id;
    }
    public String getMemberName(){ return member_name; }
}