package com.iwantodo.services.event;

public class UpdateEventCommand {
    private Long eventId;
    private boolean status;

    public Long getEventId() {
        return eventId;
    }
    public boolean isStatus() {
        return status;
    }

    public UpdateEventCommand(Long eventId, boolean status) {
        this.eventId = eventId;
        this.status = status;
    }
}
