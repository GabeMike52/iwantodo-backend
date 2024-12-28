package com.IWanToDo;

import com.iwantodo.entities.user.User;
import com.iwantodo.infra.exception.ErrorMessages;
import com.iwantodo.infra.exception.UserNotValidException;
import com.iwantodo.validators.SigninValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SigninValidatorTest {
    @Test
    public void given_valid_information_when_execute_then_no_exception() {
        //Given
        User validInfo = new User();
        validInfo.setUsername("John Doe");
        validInfo.setPassword("this-user-password");

        //When & Then
        Assertions.assertDoesNotThrow(() -> SigninValidator.execute(validInfo));
    }

    @Test
    public void given_username_empty_when_execute_throw_user_not_valid_exception() {
        //Given
        User userWithEmtpyName = new User();
        userWithEmtpyName.setUsername("");
        userWithEmtpyName.setPassword("this-user-password");

        //When & Then
        UserNotValidException exception = Assertions.assertThrows(
                UserNotValidException.class,
                () -> SigninValidator.execute(userWithEmtpyName)
        );
        Assertions.assertEquals(ErrorMessages.NAME_REQUIRED.getMessage(), exception.getMessage());
    }

    @Test
    public void given_password_empty_when_execute_throw_user_not_valid_exception() {
        //Given
        User userWithEmptyPassword = new User();
        userWithEmptyPassword.setUsername("John Doe");
        userWithEmptyPassword.setPassword("");

        //When & Then
        UserNotValidException exception = Assertions.assertThrows(
                UserNotValidException.class,
                () -> SigninValidator.execute(userWithEmptyPassword)
        );
        Assertions.assertEquals(ErrorMessages.PASSWORD_REQUIRED.getMessage(), exception.getMessage());
    }
}
