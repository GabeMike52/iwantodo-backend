package com.IWanToDo;

import com.iwantodo.entities.user.User;
import com.iwantodo.infra.exception.UserNotFoundException;
import com.iwantodo.repositories.UserRepository;
import com.iwantodo.services.user.SigninUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

public class SigninUserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SigninUserService signinUserService;

    @BeforeEach
    void seutp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void given_user_exists_when_signin_user_service_then_return_login_message() {
        //Given
        User user = new User();
        user.setId(1L);
        user.setUsername("John Doe");
        user.setEmail("john@doe.com");
        user.setPassword("raw-password");

        when(userRepository.findUserByUsername("John Doe")).thenReturn(user);
        when(passwordEncoder.matches("raw-password", user.getPassword())).thenReturn(true);

        //When
        ResponseEntity<String> response = signinUserService.execute(user);

        //Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals("User successfully signed-in", response.getBody());
        verify(userRepository, times(1)).findUserByUsername(user.getUsername());
        verify(passwordEncoder, times(1)).matches("raw-password", user.getPassword());
    }

    @Test
    public void given_user_does_not_exists_when_signin_user_service_throw_user_not_found_exception() {
        //Given
        String nonExistentUsername = "Louis";
        when(userRepository.findUserByUsername(nonExistentUsername)).thenReturn(null);

        //When
        Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            User user = new User();
            user.setUsername(nonExistentUsername);
            user.setPassword("password");
            signinUserService.execute(user);
        });

        //Then
        Assertions.assertEquals("No users found", exception.getMessage());
        verify(userRepository, times(1)).findUserByUsername(nonExistentUsername);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    public void given_user_password_does_not_match_when_user_signin_service_throw_bad_credentials_exception() {
        //Given
        User user = new User();
        user.setId(1L);
        user.setUsername("John Doe");
        user.setPassword("im-right");

        String wrongPassword = "im-wrong";

        when(userRepository.findUserByUsername("John Doe")).thenReturn(user);
        when(passwordEncoder.matches(wrongPassword, user.getPassword())).thenReturn(false);

        //When
        Exception exception = Assertions.assertThrows(BadCredentialsException.class, () -> {
            User wrongUser = new User();
            wrongUser.setUsername("John Doe");
            wrongUser.setPassword(wrongPassword);
            signinUserService.execute(wrongUser);
        });

        //Then
        Assertions.assertEquals("Username or password invalid", exception.getMessage());
        verify(userRepository, times(1)).findUserByUsername("John Doe");
        verify(passwordEncoder, times(1)).matches(wrongPassword, user.getPassword());
    }
}
