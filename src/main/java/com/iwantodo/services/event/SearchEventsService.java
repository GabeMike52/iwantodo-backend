package com.iwantodo.services.event;

import com.iwantodo.entities.event.Event;
import com.iwantodo.entities.event.EventDTO;
import com.iwantodo.infra.security.jwt.JwtUtil;
import com.iwantodo.repositories.EventRepository;
import com.iwantodo.services.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class SearchEventsService implements Query<SearchEventsCommand, List<EventDTO>> {
    private final EventRepository eventRepository;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(SearchEventsService.class);
    public SearchEventsService(EventRepository eventRepository,
                               JwtUtil jwtUtil) {
        this.eventRepository = eventRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResponseEntity<List<EventDTO>> execute(SearchEventsCommand command) {
        logger.info("Executing " + getClass() + " title: " + command.getTitle());
        String token = jwtUtil.extractToken(command.getToken());
        String username = jwtUtil.extractUsername(token);
        List<Event> events = eventRepository.findEventByTitle(username, command.getTitle());
        List<EventDTO> eventDTOs = events.stream().map(EventDTO::new).toList();
        return ResponseEntity.ok(eventDTOs);
    }
}
