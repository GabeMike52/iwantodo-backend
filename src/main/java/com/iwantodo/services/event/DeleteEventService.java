package com.iwantodo.services.event;

import com.iwantodo.entities.event.Event;
import com.iwantodo.infra.exception.EventNotFoundException;
import com.iwantodo.repositories.EventRepository;
import com.iwantodo.services.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeleteEventService implements Command<Long, Void> {
    private final EventRepository eventRepository;
    private static final Logger logger = LoggerFactory.getLogger(DeleteEventService.class);
    public DeleteEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public ResponseEntity<Void> execute(Long id) {
        logger.info("Executing " + getClass() + " id: " + id);
        Optional<Event> eventOptional = eventRepository.findById(id);
        if(eventOptional.isPresent()) {
            eventRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        throw new EventNotFoundException();
    }
}
