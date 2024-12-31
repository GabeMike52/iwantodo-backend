package com.iwantodo.controllers;

import com.iwantodo.entities.event.Event;
import com.iwantodo.entities.event.EventDTO;
import com.iwantodo.services.event.CreateEventCommand;
import com.iwantodo.services.event.CreateEventService;
import com.iwantodo.services.event.GetEventsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {
    private final CreateEventService createEventService;
    private final GetEventsService getEventsService;

    public EventController(CreateEventService createEventService,
                           GetEventsService getEventsService) {
        this.createEventService = createEventService;
        this.getEventsService = getEventsService;
    }

    @PostMapping("/create")
    public ResponseEntity<EventDTO> createEvent(@RequestHeader("Authorization") String token, @RequestBody Event event) {
        return createEventService.execute(new CreateEventCommand(token, event));
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDTO>> getEvents(@RequestHeader("Authorization") String token) {
        return getEventsService.execute(token);
    }
}
