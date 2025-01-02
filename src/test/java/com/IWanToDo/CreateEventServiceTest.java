package com.IWanToDo;

import com.iwantodo.entities.event.Event;
import com.iwantodo.entities.event.EventDTO;
import com.iwantodo.entities.user.User;
import com.iwantodo.infra.exception.ErrorMessages;
import com.iwantodo.infra.exception.UserNotValidException;
import com.iwantodo.infra.security.jwt.JwtUtil;
import com.iwantodo.repositories.EventRepository;
import com.iwantodo.repositories.UserRepository;
import com.iwantodo.services.event.CreateEventCommand;
import com.iwantodo.services.event.CreateEventService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateEventServiceTest {
    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private CreateEventService createEventService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        createEventService = spy(new CreateEventService(eventRepository, jwtUtil, userRepository));
    }

    @Test
    public void given_event_information_when_create_event_service_then_return_event_dto() {
        //Given
        String username = "john.doe";
        User user = new User();
        user.setUsername(username);

        Event validEvent = new Event();
        validEvent.setEventId(1L);
        validEvent.setTitle("Testing");
        validEvent.setDone(false);

        String jwtToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZSJ9.dummy-signature";
        String token = jwtToken.substring(7).trim();

        CreateEventCommand command = new CreateEventCommand(jwtToken, validEvent);

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(username);
        when(jwtUtil.extractToken(jwtToken)).thenReturn(token);
        when(jwtUtil.extractUsername(token)).thenReturn(username);
        when(jwtUtil.getClaims(token)).thenReturn(claims);
        when(userRepository.findUserByUsername(username)).thenReturn(user);
        when(eventRepository.save(any(Event.class))).thenReturn(validEvent);

        //When
        ResponseEntity<EventDTO> response = createEventService.execute(command);

        //Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(new EventDTO(validEvent), response.getBody());
        verify(userRepository, times(1)).findUserByUsername(username);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    public void given_username_not_found_throw_user_not_valid_exception() {
        //Given
        String emptyUsername = "";
        User user = new User();
        user.setUsername("john.doe");

        Event event = new Event();
        event.setEventId(1L);
        event.setTitle("Testing");
        event.setDone(false);

        String jwtToken = "Bearer jwt-token-john.doe";
        String token = jwtToken.substring(7).trim();

        CreateEventCommand command = new CreateEventCommand(jwtToken, event);

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(emptyUsername);
        when(jwtUtil.extractToken(jwtToken)).thenReturn(token);
        when(jwtUtil.extractUsername(token)).thenReturn(emptyUsername);
        when(jwtUtil.getClaims(token)).thenReturn(claims);
        when(userRepository.findUserByUsername(emptyUsername)).thenReturn(null);

        //When & Then
        UserNotValidException exception = Assertions.assertThrows(
                UserNotValidException.class,
                () -> createEventService.execute(command)
        );
        Assertions.assertEquals(ErrorMessages.NOT_AUTHENTICATED.getMessage(), exception.getMessage());
    }
}
