package com.IWanToDo;

import com.iwantodo.entities.event.Event;
import com.iwantodo.entities.user.User;
import com.iwantodo.infra.exception.ErrorMessages;
import com.iwantodo.infra.exception.EventNotFoundException;
import com.iwantodo.repositories.EventRepository;
import com.iwantodo.services.event.DeleteEventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class DeleteEventServiceTest {
    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private DeleteEventService deleteEventService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void given_event_exists_when_delete_event_service_then_return_delete_event() {
        //Given
        User user = new User("john.doe", "john@doe.com", "this-user-password");

        Event event = new Event();
        event.setEventId(1L);
        event.setTitle("Testing");
        event.setDone(true);
        event.setOwner(user);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        //When
        ResponseEntity<Void> response = deleteEventService.execute(1L);

        //Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(204, response.getStatusCode().value());
        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).deleteById(1L);
    }

    @Test
    public void given_event_does_not_exist_when_delete_event_service_throw_event_not_found_exception() {
        //Given
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        //When & Then
        EventNotFoundException exception = Assertions.assertThrows(
                EventNotFoundException.class,
                () -> deleteEventService.execute(eventId)
        );
        Assertions.assertEquals(ErrorMessages.EVENT_NOT_FOUND.getMessage(), exception.getMessage());
    }
}
