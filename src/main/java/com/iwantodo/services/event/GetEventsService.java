package com.iwantodo.services.event;

import com.iwantodo.entities.event.Event;
import com.iwantodo.entities.event.EventDTO;
import com.iwantodo.infra.security.jwt.JwtUtil;
import com.iwantodo.repositories.EventRepository;
import com.iwantodo.services.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetEventsService implements Query<String, List<EventDTO>> {
    private final EventRepository eventRepository;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(GetEventsService.class);
    public GetEventsService(EventRepository eventRepository,
                            JwtUtil jwtUtil) {
        this.eventRepository = eventRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResponseEntity<List<EventDTO>> execute(String header) {
        logger.info("Executing " + getClass());
        String token = jwtUtil.extractToken(header);
        String username = jwtUtil.extractUsername(token);
        List<Event> events = eventRepository.findByOwner(username);
        List<EventDTO> eventDTOs = events.stream().map(EventDTO::new).toList();
        return ResponseEntity.ok(eventDTOs);
    }
}
