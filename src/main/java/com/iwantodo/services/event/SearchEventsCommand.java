package com.iwantodo.services.event;

public class SearchEventsCommand {
    private String token;
    private String title;

    public String getToken() {
        return token;
    }
    public String getTitle() {
        return title;
    }

    public SearchEventsCommand(String token, String title) {
        this.token = token;
        this.title = title;
    }
}
