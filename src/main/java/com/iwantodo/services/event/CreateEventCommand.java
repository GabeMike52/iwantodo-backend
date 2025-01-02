package com.iwantodo.services.event;

import com.iwantodo.entities.event.Event;

public class CreateEventCommand {
    private String header;
    private Event event;

    public String getHeader() {
        return header;
    }
    public Event getEvent() {
        return event;
    }

    public CreateEventCommand(String header, Event event) {
        this.header = header;
        this.event = event;
    }
}
