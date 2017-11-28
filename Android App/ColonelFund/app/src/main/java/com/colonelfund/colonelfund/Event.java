package com.colonelfund.colonelfund;

import org.json.JSONObject;
import org.json.JSONException;

/**
 * Basic Event Object
 */
public class Event {
    private String title;
    private String associatedMember;
    private String eventDate;
    private double fundGoal;
    private double currentFunds;
    /**
     * @param title
     * @param associatedMember
     * @param eventDate
     * @param fundGoal
     * @param currentFunds
     */
    public Event(String title, String associatedMember, String eventDate, double fundGoal, double currentFunds) {
        super();
        this.title = title;
        this.associatedMember = associatedMember;
        this.eventDate = eventDate;
        this.fundGoal = fundGoal;
        this.currentFunds = currentFunds;
    }
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return the associatedMember
     */
    public String getAssociatedMember() {
        return associatedMember;
    }
    /**
     * @param associatedMember the associatedMember to set
     */
    public void setAssociatedMember(String associatedMember) {
        this.associatedMember = associatedMember;
    }
    /**
     * @return the eventDate
     */
    public String getEventDate() {
        return eventDate;
    }
    /**
     * @param eventDate the eventDate to set
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
    /**
     * @return the fundGoal
     */
    public double getFundGoal() {
        return fundGoal;
    }
    /**
     * @param fundGoal the fundGoal to set
     */
    public void setFundGoal(double fundGoal) {
        this.fundGoal = fundGoal;
    }
    /**
     * @return the currentFunds
     */
    public double getCurrentFunds() {
        return currentFunds;
    }
    /**
     * @param currentFunds the currentFunds to set
     */
    public void setCurrentFunds(double currentFunds) {
        this.currentFunds = currentFunds;
    }
    /**
     * Get a JSONObject representation of Event
     * @return JSONObject
     */
    public JSONObject toJson() throws JSONException {
        JSONObject JsonObj = new JSONObject();
        JsonObj.put("Title",title);
        JsonObj.put("AssociatedMember",associatedMember);
        JsonObj.put("EventDate",eventDate);
        JsonObj.put("FundGoal",fundGoal);
        JsonObj.put("CurrentFunds",currentFunds);
        return JsonObj;
    }
    /**
     * Constructor with JSON Object.
     * @param jsonObject
     */
    public Event(JSONObject jsonObject) throws JSONException {
        this.title = jsonObject.getString("Title");
        this.associatedMember = jsonObject.getString("AssociatedMember");
        this.eventDate = jsonObject.getString("EventDate");
        this.fundGoal = jsonObject.getDouble("FundGoal");
        this.currentFunds = jsonObject.getDouble("CurrentFunds");
    }
}
