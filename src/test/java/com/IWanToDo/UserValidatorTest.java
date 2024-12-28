package com.IWanToDo;

import com.iwantodo.entities.user.User;
import com.iwantodo.infra.exception.ErrorMessages;
import com.iwantodo.infra.exception.UserNotValidException;
import com.iwantodo.validators.UserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserValidatorTest {
    @Test
    public void given_valid_user_when_execute_then_no_exception() {
        //Given
        User validUser = new User();
        validUser.setId(1L);
        validUser.setUsername("John Doe");
        validUser.setEmail("john@doe.com");
        validUser.setPassword("this-user-password");

        //When & Then
        Assertions.assertDoesNotThrow(() -> UserValidator.execute(validUser));
    }

    @Test
    public void given_user_with_empty_username_when_execute_throw_user_not_valid_exception() {
        //Given
        User userWithEmptyName = new User();
        userWithEmptyName.setId(1L);
        userWithEmptyName.setUsername("");
        userWithEmptyName.setEmail("nousername@wrong.com");
        userWithEmptyName.setPassword("this-user-password");

        //When & Then
        UserNotValidException exception = Assertions.assertThrows(
                UserNotValidException.class,
                () -> UserValidator.execute(userWithEmptyName)
        );
        Assertions.assertEquals(ErrorMessages.NAME_REQUIRED.getMessage(), exception.getMessage());
    }

    @Test
    public void given_user_with_invalid_email_when_execute_throw_user_not_valid_exception() {
        //Given
        User userWithInvalidEmail = new User();
        userWithInvalidEmail.setId(1L);
        userWithInvalidEmail.setUsername("John Doe");
        userWithInvalidEmail.setEmail("johndoeman");
        userWithInvalidEmail.setPassword("this-user-password");

        //When & Then
        UserNotValidException exception = Assertions.assertThrows(
                UserNotValidException.class,
                () -> UserValidator.execute(userWithInvalidEmail)
        );
        Assertions.assertEquals(ErrorMessages.EMAIL_NOT_VALID.getMessage(), exception.getMessage());
    }

    @Test
    public void given_user_with_empty_password_when_execute_throw_user_not_valid_exception() {
        //Given
        User userWithEmptyPassword = new User();
        userWithEmptyPassword.setId(1L);
        userWithEmptyPassword.setUsername("John Doe");
        userWithEmptyPassword.setEmail("john@doe.com");
        userWithEmptyPassword.setPassword("");

        //When & Then
        UserNotValidException exception = Assertions.assertThrows(
                UserNotValidException.class,
                () -> UserValidator.execute(userWithEmptyPassword)
        );
        Assertions.assertEquals(ErrorMessages.PASSWORD_REQUIRED.getMessage(), exception.getMessage());
    }

    @Test
    public void given_user_with_short_password_when_execute_throw_user_not_valid_exception() {
        //Given
        User userWithShortPassword = new User();
        userWithShortPassword.setId(1L);
        userWithShortPassword.setUsername("John Doe");
        userWithShortPassword.setEmail("john@doe.com");
        userWithShortPassword.setPassword("short");

        //When & Then
        UserNotValidException exception = Assertions.assertThrows(
                UserNotValidException.class,
                () -> UserValidator.execute(userWithShortPassword)
        );
        Assertions.assertEquals(ErrorMessages.PASSWORD_LENGTH.getMessage(), exception.getMessage());
    }
}
