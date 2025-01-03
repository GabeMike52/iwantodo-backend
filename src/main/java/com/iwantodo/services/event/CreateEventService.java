package com.iwantodo.services.event;

import com.iwantodo.entities.event.Event;
import com.iwantodo.entities.event.EventDTO;
import com.iwantodo.entities.user.User;
import com.iwantodo.infra.exception.ErrorMessages;
import com.iwantodo.infra.exception.UserNotValidException;
import com.iwantodo.infra.security.jwt.JwtUtil;
import com.iwantodo.repositories.EventRepository;
import com.iwantodo.repositories.UserRepository;
import com.iwantodo.services.Command;
import com.iwantodo.validators.EventValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class CreateEventService implements Command<CreateEventCommand, EventDTO> {
    private final EventRepository eventRepository;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(CreateEventService.class);
    private final UserRepository userRepository;

    public CreateEventService(EventRepository eventRepository,
                              JwtUtil jwtUtil,
                              UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<EventDTO> execute(CreateEventCommand command) {
        logger.info("Executing " + getClass() + " event: " + command.getEvent());
        EventValidator.execute(command.getEvent());
        String token = jwtUtil.extractToken(command.getHeader());
        String username = jwtUtil.extractUsername(token);
        if(username == null || username.isEmpty()) {
            throw new UserNotValidException(ErrorMessages.NOT_AUTHENTICATED.getMessage());
        }
        User user = userRepository.findUserByUsername(username);
        Event event = command.getEvent();
        event.setOwner(user);
        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(new EventDTO(savedEvent));
    }
}
