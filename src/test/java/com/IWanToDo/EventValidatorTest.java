package com.IWanToDo;

import com.iwantodo.entities.event.Event;
import com.iwantodo.entities.user.User;
import com.iwantodo.infra.exception.ErrorMessages;
import com.iwantodo.infra.exception.EventNotValidException;
import com.iwantodo.validators.EventValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;

public class EventValidatorTest {
    @Test
    public void given_valid_event_when_execute_then_no_exception() {
        //Given
        User user = new User("john.doe", "john@doe.com", "this-user-password");
        Event validEvent = new Event("Valid event", true, user);
        validEvent.setEventId(1L);

        //When & Then
        Assertions.assertDoesNotThrow(() -> EventValidator.execute(validEvent));
    }

    @Test
    public void given_event_with_empty_title_when_execute_throw_event_not_valid_exception() {
        //Given
        User user = new User("john.doe", "john@doe.com", "this-user-password");
        Event eventWithEmptyTitle = new Event("", true, user);
        eventWithEmptyTitle.setEventId(1L);

        //When & Then
        EventNotValidException exception = Assertions.assertThrows(
                EventNotValidException.class,
                () -> EventValidator.execute(eventWithEmptyTitle)
        );
        Assertions.assertEquals(ErrorMessages.TITLE_REQUIRED.getMessage(), exception.getMessage());
    }

    @Test
    public void given_event_with_null_done_field_when_execute_throw_event_not_valid_exception() {
        //Given
        User user = new User("john.doe", "john@doe.com", "this-user-password");
        Event eventWithNullDoneField = new Event("Test Event", null, user);
        eventWithNullDoneField.setEventId(1L);

        //When & Then
        EventNotValidException exception = Assertions.assertThrows(
                EventNotValidException.class,
                () -> EventValidator.execute(eventWithNullDoneField)
        );
        Assertions.assertEquals(ErrorMessages.STATUS_REQUIRED.getMessage(), exception.getMessage());
    }
}
