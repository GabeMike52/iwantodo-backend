package com.IWanToDo;

import com.iwantodo.entities.event.Event;
import com.iwantodo.entities.event.EventDTO;
import com.iwantodo.entities.user.User;
import com.iwantodo.infra.security.jwt.JwtUtil;
import com.iwantodo.repositories.EventRepository;
import com.iwantodo.services.event.GetEventsService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;

public class GetEventsServiceTest {
    @Mock
    private EventRepository eventRepository;

    @Mock JwtUtil jwtUtil;

    @InjectMocks
    private GetEventsService getEventsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void given_auth_token_when_get_events_service_then_return_list_of_event_dtos() {
        //Given
        User user = new User("john.doe", "john@doe.com", "this-user-password");
        Event eventOne = new Event();
        eventOne.setEventId(1L);
        eventOne.setTitle("Testing");
        eventOne.setDone(false);
        eventOne.setOwner(user);
        Event eventTwo = new Event();
        eventTwo.setEventId(2L);
        eventTwo.setTitle("Testing two");
        eventTwo.setDone(true);
        eventTwo.setOwner(user);
        List<Event> events = List.of(eventOne, eventTwo);

        String header = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZSJ9.dummy-signature";
        String token = header.substring(7).trim();

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(user.getUsername());
        when(jwtUtil.extractToken(header)).thenReturn(token);
        when(jwtUtil.extractUsername(token)).thenReturn(user.getUsername());
        when(jwtUtil.getClaims(token)).thenReturn(claims);
        when(eventRepository.findByOwner(user.getUsername())).thenReturn(events);

        List<EventDTO> eventDTOs = events.stream().map(EventDTO::new).toList();

        //When
        ResponseEntity<List<EventDTO>> response = getEventsService.execute(header);

        //Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(eventDTOs, response.getBody());
        verify(eventRepository, times(1)).findByOwner(user.getUsername());
    }
}
