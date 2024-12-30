package com.iwantodo.validators;

import com.iwantodo.entities.user.User;
import com.iwantodo.infra.exception.ErrorMessages;
import com.iwantodo.infra.exception.UserNotValidException;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
    private UserValidator() {

    }

    public static boolean isEmailValid(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isUsernameValid(String username) {
        String usernameRegex = "^[a-z._]+$";
        Pattern pattern = Pattern.compile(usernameRegex);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public static void execute(User user) {
        if (StringUtils.isEmpty(user.getUsername())) {
            throw new UserNotValidException(ErrorMessages.NAME_REQUIRED.getMessage());
        }
        if(!isUsernameValid(user.getUsername())) {
            throw new UserNotValidException(ErrorMessages.NAME_INVALID.getMessage());
        }
        if (!isEmailValid(user.getEmail())) {
            throw new UserNotValidException(ErrorMessages.EMAIL_NOT_VALID.getMessage());
        }
        if(StringUtils.isEmpty(user.getPassword())) {
            throw new UserNotValidException(ErrorMessages.PASSWORD_REQUIRED.getMessage());
        }
        if(user.getPassword().length() < 8) {
            throw new UserNotValidException(ErrorMessages.PASSWORD_LENGTH.getMessage());
        }
    }
}
