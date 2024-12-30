package com.iwantodo.validators;

import com.iwantodo.entities.event.Event;
import com.iwantodo.infra.exception.ErrorMessages;
import com.iwantodo.infra.exception.EventNotValidException;
import org.springframework.util.StringUtils;

public class EventValidator {
    private EventValidator() {

    }

    public static void execute(Event event) {
        if(StringUtils.isEmpty(event.getTitle())) {
            throw new EventNotValidException(ErrorMessages.TITLE_REQUIRED.getMessage());
        }
        if(event.getDone() == null) {
            throw new EventNotValidException(ErrorMessages.STATUS_REQUIRED.getMessage());
        }
    }
}
