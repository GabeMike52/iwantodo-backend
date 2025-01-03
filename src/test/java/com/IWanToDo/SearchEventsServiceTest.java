package com.IWanToDo;

import com.iwantodo.entities.event.Event;
import com.iwantodo.entities.event.EventDTO;
import com.iwantodo.entities.user.User;
import com.iwantodo.infra.security.jwt.JwtUtil;
import com.iwantodo.repositories.EventRepository;
import com.iwantodo.services.event.SearchEventsCommand;
import com.iwantodo.services.event.SearchEventsService;
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

public class SearchEventsServiceTest {
    @Mock
    private EventRepository eventRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private SearchEventsService searchEventsService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void given_token_and_title_when_search_events_service_then_return_list_of_event_dtos() {
        //Given
        String title = "Testing";
        String username = "john.doe";
        User user = new User(username, "john@doe.com", "this-user-password");
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
        List<EventDTO> eventDTOs = events.stream().map(EventDTO::new).toList();

        String header = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZSJ9.dummy-signature";
        String token = header.substring(7).trim();

        SearchEventsCommand command = new SearchEventsCommand(header, title);

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(username);
        when(jwtUtil.extractToken(header)).thenReturn(token);
        when(jwtUtil.extractUsername(token)).thenReturn(username);
        when(jwtUtil.getClaims(token)).thenReturn(claims);
        when(eventRepository.findEventByTitle(username, title)).thenReturn(events);

        //When
        ResponseEntity<List<EventDTO>> response = searchEventsService.execute(command);

        //Then
        Assertions.assertEquals(eventDTOs, response.getBody());
        verify(jwtUtil, times(1)).extractToken(header);
        verify(jwtUtil, times(1)).extractUsername(token);
        verify(eventRepository, times(1)).findEventByTitle(username, title);
    }
}
