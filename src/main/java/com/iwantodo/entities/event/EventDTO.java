package com.iwantodo.entities.event;

import com.iwantodo.entities.user.User;

import java.util.Objects;

public class EventDTO {
    private Long eventId;
    private String title;
    private Boolean done;
    private User owner;

    public EventDTO(Event event) {
        this.title = event.getTitle();
        this.done = event.getDone();
        this.owner = event.getOwner();
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
    public Boolean getDone() {
        return done;
    }
    public void setDone(Boolean done) {
        this.done = done;
    }
    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        EventDTO that = (EventDTO) o;
        return Objects.equals(eventId, that.eventId) &&
                Objects.equals(title, that.title) &&
                Objects.equals(done, that.done) &&
                Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, title, done, owner);
    }
}
