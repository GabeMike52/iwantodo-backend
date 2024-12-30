package com.iwantodo.entities.event;

import com.iwantodo.entities.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;
    private String title;
    private Boolean done;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private User owner;

    public Event() {

    }

    public Event(String title, Boolean done, User owner) {
        this.title = title;
        this.done = done;
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
    public Boolean getDone() {
        return done;
    }
    public void setDone(Boolean done) {
        this.done = done;
    }
}
