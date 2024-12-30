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
        validUser.setUsername("john.doe");
        validUser.setEmail("john@doe.com");
        validUser.setPassword("this-user-password");

        //When & Then
        Assertions.assertDoesNotThrow(() -> UserValidator.execute(validUser));
    }

    @Test
    public void given_user_with_empty_username_when_execute_throw_user_not_valid_exception() {
        //Given
        User userWithEmptyName = new User();
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
    public void given_user_with_invalid_username_when_execute_throw_user_not_valid_exception() {
        //Given
        User userWithInvalidUsername = new User();
        userWithInvalidUsername.setUsername("John Doe");
        userWithInvalidUsername.setEmail("john@doe.com");
        userWithInvalidUsername.setPassword("this-user-password");

        //When & Then
        UserNotValidException exception = Assertions.assertThrows(
                UserNotValidException.class,
                () -> UserValidator.execute(userWithInvalidUsername)
        );
        Assertions.assertEquals(ErrorMessages.NAME_INVALID.getMessage(), exception.getMessage());
    }

    @Test
    public void given_user_with_invalid_email_when_execute_throw_user_not_valid_exception() {
        //Given
        User userWithInvalidEmail = new User();
        userWithInvalidEmail.setUsername("jhon.doe");
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
        userWithEmptyPassword.setUsername("jhon.doe");
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
        userWithShortPassword.setUsername("jhon.doe");
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
