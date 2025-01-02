package com.iwantodo.infra.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EventNotFoundException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(EventNotFoundException.class);
    public EventNotFoundException() {
        super(ErrorMessages.EVENT_NOT_FOUND.getMessage());
        logger.error("Exception " + getClass() + " thrown");
    }
}
