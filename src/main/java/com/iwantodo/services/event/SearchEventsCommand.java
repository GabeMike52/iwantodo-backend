package com.iwantodo.services.event;

public class SearchEventsCommand {
    private String header;
    private String title;

    public String getHeader() {
        return header;
    }
    public String getTitle() {
        return title;
    }

    public SearchEventsCommand(String header, String title) {
        this.header = header;
        this.title = title;
    }
}
