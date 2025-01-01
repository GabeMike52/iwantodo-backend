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
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;

public class GetEventsServiceTest {
    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private GetEventsService getEventsService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void given_auth_token_when_get_events_service_then_return_list_of_event_dtos() {
        //Given
        User user = new User("john.doe", "john@doe.com", "this-user-password");
        Event eventOne = new Event("Testing", false, user);
        Event eventTwo = new Event("Testing 2", true, user);
        List<Event> events = List.of(eventOne, eventTwo);

        String jwtToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZSJ9.dummy-signature";
        String token = jwtToken.substring(7).trim();

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(user.getUsername());
        try (MockedStatic<JwtUtil> mockedJwtUtil = mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.extractToken(jwtToken)).thenReturn(token);
            mockedJwtUtil.when(() -> JwtUtil.extractUsername(token)).thenReturn(user.getUsername());
            mockedJwtUtil.when(() -> JwtUtil.getClaims(token)).thenReturn(claims);
            when(eventRepository.findByOwner(user.getUsername())).thenReturn(events);

            List<EventDTO> eventDTOs = events.stream().map(EventDTO::new).toList();

            //When
            ResponseEntity<List<EventDTO>> response = getEventsService.execute(jwtToken);

            //Then
            Assertions.assertNotNull(response);
            Assertions.assertEquals(eventDTOs, response.getBody());
            verify(eventRepository, times(1)).findByOwner(user.getUsername());
        }
    }
}
