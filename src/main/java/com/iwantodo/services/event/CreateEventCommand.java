package com.iwantodo.services.event;

import com.iwantodo.entities.event.Event;

public class CreateEventCommand {
    private String token;
    private Event event;

    public String getToken() {
        return token;
    }
    public Event getEvent() {
        return event;
    }

    public CreateEventCommand(String token, Event event) {
        this.token = token;
        this.event = event;
    }
}
