package com.colonelfund.colonelfund;

import org.json.JSONObject;
import org.json.JSONException;
import java.io.Serializable;

/**
 * Basic Event Object that holds all of the information for an event.
 */
public class Event implements Serializable {
    private static final long serialVersionUID = -8645384342222081215L;
    private String title;
    private String associatedMember;
    private String associatedEmail;
    private String eventDate;
    private double fundGoal;
    private double currentFunds;
    private String description;
    private String type;

    /**
     * @param title of event
     * @param associatedMember of event
     * @param eventDate of event
     * @param fundGoal of event
     * @param currentFunds of event
     * @param description of event
     */
    public Event(String title,
                 String associatedMember,
                 String eventDate,
                 double fundGoal,
                 double currentFunds,
                 String description,
                 String type) {
        super();
        this.title = title;
        this.associatedMember = associatedMember;
        this.eventDate = eventDate;
        this.fundGoal = fundGoal;
        this.currentFunds = currentFunds;
        this.description = description;
        this.type = type;
    }

    /**
     * @param title of event
     * @param associatedMember of event
     * @param associatedEmail of event
     * @param eventDate of event
     * @param fundGoal of event
     * @param currentFunds of event
     * @param description of event
     */
    public Event(String title,
                 String associatedMember,
                 String associatedEmail,
                 String eventDate,
                 double fundGoal,
                 double currentFunds,
                 String description,
                 String type) {
        super();
        this.title = title;
        this.associatedMember = associatedMember;
        this.associatedEmail = associatedEmail;
        this.eventDate = eventDate;
        this.fundGoal = fundGoal;
        this.currentFunds = currentFunds;
        this.description = description;
        this.type = type;
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
    public void setAssociatedEmail(String associatedEmail) {
        this.associatedEmail = associatedEmail;
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
    public String getAssociatedEmail() {return associatedEmail;}

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
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * Set the description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return the title
     */
    public String getType() {
        return type;
    }
    /**
     * @param type the title to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * Get a JSONObject representation of Event
     * @return JSONObject of an event
     */
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("title",title);
        jsonObj.put("associatedMember",associatedMember);
        jsonObj.put("associatedEmail",associatedEmail);
        jsonObj.put("eventDate",eventDate);
        jsonObj.put("fundGoal",fundGoal);
        jsonObj.put("currentFunds",currentFunds);
        jsonObj.put("description",description);
        jsonObj.put("type",type);
        return jsonObj;
    }
    /**
     * Constructor with JSON Object.
     * @param jsonObject of an event
     */
    public Event(JSONObject jsonObject) throws JSONException {
        this.title = jsonObject.getString("title");
        this.associatedMember = jsonObject.getString("associatedMember");
        this.associatedEmail = jsonObject.getString("associatedEmail");
        this.eventDate = jsonObject.getString("eventDate");
        this.fundGoal = jsonObject.getDouble("fundGoal");
        this.currentFunds = jsonObject.getDouble("currentFunds");
        this.description = jsonObject.getString("description");
        this.type = jsonObject.getString("type");
    }
}
