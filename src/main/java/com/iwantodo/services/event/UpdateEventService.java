package com.iwantodo.services.event;

import com.iwantodo.entities.event.Event;
import com.iwantodo.entities.event.EventDTO;
import com.iwantodo.infra.exception.EventNotFoundException;
import com.iwantodo.repositories.EventRepository;
import com.iwantodo.services.Command;
import com.iwantodo.validators.EventValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateEventService implements Command<UpdateEventCommand, EventDTO> {
    private final EventRepository eventRepository;
    private static final Logger logger = LoggerFactory.getLogger(UpdateEventService.class);
    public UpdateEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public ResponseEntity<EventDTO> execute(UpdateEventCommand command) {
        logger.info("Executing " + getClass() + " done: " + command.isStatus());
        Optional<Event> eventOptional = eventRepository.findById(command.getEventId());
        if(eventOptional.isPresent()) {
            Event event = new Event();
            event.setEventId(eventOptional.get().getEventId());
            event.setTitle(eventOptional.get().getTitle());
            event.setDone(command.isStatus());
            event.setOwner(eventOptional.get().getOwner());
            EventValidator.execute(event);
            eventRepository.save(event);
            return ResponseEntity.ok(new EventDTO(event));
        }
        throw new EventNotFoundException();
    }
}
