package com.colonelfund.colonelfund;

/**
 * Event list Item Model class.
 */
class EventListModel {
    private String title;
    private String type;
    private String associatedMember;
    private String associatedEmail;
    private String eventDate;
    private Double goalProgress;
    private String eventDescription;

    /**
     * Constructor for Event List Model with 6 args
     *
     * @param title            of event
     * @param type             of event
     * @param associatedMember of event
     * @param eventDate        of event
     * @param goalProgress     of event
     * @param eventDesc        of event
     */
    public EventListModel(String title, String type, String associatedMember,
                          String associatedEmail, String eventDate,
                          Double goalProgress, String eventDesc) {
        super();
        this.title = title;
        this.type = type;
        this.associatedMember = associatedMember;
        this.associatedEmail = associatedEmail;
        this.eventDate = eventDate;
        this.goalProgress = (goalProgress * 100);
        this.eventDescription = eventDesc;
    }

    /**
     * @return eventType
     */
    public String getType() {
        return type;
    }

    /**
     * @return eventTitle
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return eventAssociatedMember
     */
    public String getAssociatedMember() {
        return associatedMember;
    }

    /**
     * @return eventDate
     */
    public String getAssociatedEmail() {
        return associatedEmail;
    }

    public String getEventDate() {
        return eventDate;
    }

    /**
     * @return eventGoalProgress
     */
    public Double getGoalProgress() {
        return goalProgress;
    }

    /**
     * @return eventDescription
     */
    public String getEventDesc() {
        return eventDescription;
    }
}
