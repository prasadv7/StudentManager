package com.example.studentactivitysystem;

public class EventItem {
    private String eventName;
    private String eventDetails;
    private String eventDate;

    public EventItem(String eventName, String eventDetails, String eventDate) {
        this.eventName = eventName;
        this.eventDetails = eventDetails;
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public String getEventDate() {
        return eventDate;
    }
}
