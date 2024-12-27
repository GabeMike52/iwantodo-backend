package com.iwantodo.validators;

import com.iwantodo.entities.user.User;
import com.iwantodo.infra.exception.ErrorMessages;
import com.iwantodo.infra.exception.UserNotValidException;
import org.springframework.util.StringUtils;

public class SigninValidator {
    private SigninValidator() {

    }

    public static void execute(User user) {
        if(StringUtils.isEmpty(user.getUsername())) {
            throw new UserNotValidException(ErrorMessages.NAME_REQUIRED.getMessage());
        }
        if(StringUtils.isEmpty(user.getPassword())) {
            throw new UserNotValidException(ErrorMessages.PASSWORD_REQUIRED.getMessage());
        }
    }
}
