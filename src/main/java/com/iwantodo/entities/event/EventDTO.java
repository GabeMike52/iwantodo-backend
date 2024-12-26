package com.iwantodo.entities.event;

import com.iwantodo.entities.user.User;

public class EventDTO {
    private Long eventId;
    private String title;
    private User owner;

    public EventDTO(String title, User owner) {
        this.title = title;
        this.owner = owner;
    }

    public Long getEventId() {
        return eventId;
    }
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }
}
