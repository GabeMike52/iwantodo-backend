package com.IWanToDo;

import com.iwantodo.entities.event.Event;
import com.iwantodo.entities.event.EventDTO;
import com.iwantodo.entities.user.User;
import com.iwantodo.infra.exception.ErrorMessages;
import com.iwantodo.infra.exception.EventNotFoundException;
import com.iwantodo.repositories.EventRepository;
import com.iwantodo.services.event.UpdateEventCommand;
import com.iwantodo.services.event.UpdateEventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class UpdateEventServiceTest {
    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private UpdateEventService updateEventService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void given_event_exists_when_update_event_service_then_return_event_dto() {
        //Given
        User user = new User("john.doe", "john@doe.com", "this-user-password");
        Event event = new Event();
        event.setEventId(1L);
        event.setTitle("Testing");
        event.setDone(false);
        event.setOwner(user);

        UpdateEventCommand command = new UpdateEventCommand(event.getEventId(), true);

        Event updatedEvent = new Event();
        updatedEvent.setEventId(1L);
        updatedEvent.setTitle(event.getTitle());
        updatedEvent.setDone(true);
        updatedEvent.setOwner(event.getOwner());

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        //When
        ResponseEntity<EventDTO> response = updateEventService.execute(command);

        //Then
        Assertions.assertEquals(new EventDTO(updatedEvent), response.getBody());
        verify(eventRepository, times(1)).findById(1L);
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(eventRepository).save(captor.capture());
        Event capturedEvent = captor.getValue();
        Assertions.assertEquals(updatedEvent.getEventId(), capturedEvent.getEventId());
        Assertions.assertEquals(updatedEvent.getTitle(), capturedEvent.getTitle());
        Assertions.assertEquals(updatedEvent.getDone(), capturedEvent.getDone());
        Assertions.assertEquals(updatedEvent.getOwner(), capturedEvent.getOwner());
    }

    @Test
    public void given_event_does_not_exist_when_update_event_service_throw_event_not_found_exception() {
        //Given
        UpdateEventCommand command = new UpdateEventCommand(1L, true);
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        //When & Then
        EventNotFoundException exception = Assertions.assertThrows(
                EventNotFoundException.class,
                () -> updateEventService.execute(command)
        );
        Assertions.assertEquals(ErrorMessages.EVENT_NOT_FOUND.getMessage(), exception.getMessage());
    }

}
