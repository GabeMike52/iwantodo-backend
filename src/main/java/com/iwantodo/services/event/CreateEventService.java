package com.iwantodo.services.event;

import com.iwantodo.entities.event.Event;
import com.iwantodo.entities.event.EventDTO;
import com.iwantodo.entities.user.User;
import com.iwantodo.infra.exception.ErrorMessages;
import com.iwantodo.infra.exception.UserNotFoundException;
import com.iwantodo.infra.exception.UserNotValidException;
import com.iwantodo.repositories.EventRepository;
import com.iwantodo.repositories.UserRepository;
import com.iwantodo.services.Command;
import com.iwantodo.validators.EventValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class CreateEventService implements Command<Event, EventDTO> {
    private final EventRepository eventRepository;
    private static final Logger logger = LoggerFactory.getLogger(CreateEventService.class);
    private final UserRepository userRepository;

    public CreateEventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null;
    }

    @Override
    public ResponseEntity<EventDTO> execute(Event event) {
        logger.info("Executing " + getClass() + " event: ");
        EventValidator.execute(event);
        String username = getAuthenticatedUsername();
        if(username == null || username.isEmpty()) {
            throw new UserNotValidException(ErrorMessages.NOT_AUTHENTICATED.getMessage());
        }
        User user = userRepository.findUserByUsername(username);
        event.setOwner(user);
        Event savedEvent = eventRepository.save(new Event(event.getTitle(), event.getDone(), event.getOwner()));
        return ResponseEntity.ok(new EventDTO(savedEvent));
    }
}
