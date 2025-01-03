package com.iwantodo.controllers;

import com.iwantodo.entities.event.Event;
import com.iwantodo.entities.event.EventDTO;
import com.iwantodo.services.event.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {
    private final CreateEventService createEventService;
    private final GetEventsService getEventsService;
    private final SearchEventsService searchEventsService;
    private final UpdateEventService updateEventService;
    private final DeleteEventService deleteEventService;

    public EventController(CreateEventService createEventService,
                           GetEventsService getEventsService,
                           SearchEventsService searchEventsService, UpdateEventService updateEventService, DeleteEventService deleteEventService) {
        this.createEventService = createEventService;
        this.getEventsService = getEventsService;
        this.searchEventsService = searchEventsService;
        this.updateEventService = updateEventService;
        this.deleteEventService = deleteEventService;
    }

    @PostMapping("/create")
    public ResponseEntity<EventDTO> createEvent(@RequestHeader("Authorization") String header, @RequestBody Event event) {
        return createEventService.execute(new CreateEventCommand(header, event));
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDTO>> getEvents(@RequestHeader("Authorization") String header) {
        return getEventsService.execute(header);
    }

    @GetMapping("/events/filter")
    public ResponseEntity<List<EventDTO>> searchEvents(@RequestHeader("Authorization") String header, @RequestBody String title) {
        return searchEventsService.execute(new SearchEventsCommand(header, title));
    }

    @PatchMapping("/event{:eventId}")
    public ResponseEntity<EventDTO> changeEventStatus(@RequestParam Long eventId, @RequestBody boolean status) {
        return updateEventService.execute(new UpdateEventCommand(eventId, status));
    }

    @DeleteMapping("/event{:eventId}")
    public ResponseEntity<Void> deleteEvent(@RequestParam Long eventId) {
        return deleteEventService.execute(eventId);
    }
}
