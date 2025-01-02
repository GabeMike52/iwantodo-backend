package com.IWanToDo;

import com.iwantodo.entities.user.User;
import com.iwantodo.infra.exception.ErrorMessages;
import com.iwantodo.infra.exception.UserNotFoundException;
import com.iwantodo.infra.security.jwt.JwtUtil;
import com.iwantodo.repositories.UserRepository;
import com.iwantodo.services.user.SigninUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.mockito.Mockito.*;

public class SigninUserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private SigninUserService signinUserService;

    @BeforeEach
    void seutp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void given_user_exists_when_signin_user_service_then_return_token() {
        //Given
        User user = new User();
        user.setUsername("john.doe");
        user.setEmail("john@doe.com");
        user.setPassword("raw-password");

        String jwtToken = "jwtTokenExample";

        when(userRepository.findUserByUsername("john.doe")).thenReturn(user);
        when(passwordEncoder.matches("raw-password", user.getPassword())).thenReturn(true);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(authToken)).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), List.of()));
        mockStatic(JwtUtil.class).when(() -> jwtUtil.generateToken(any())).thenReturn(jwtToken);

        //When
        ResponseEntity<String> response = signinUserService.execute(user);

        //Then
        Assertions.assertEquals(jwtToken, response.getBody());
        verify(userRepository, times(1)).findUserByUsername(user.getUsername());
        verify(passwordEncoder, times(1)).matches("raw-password", user.getPassword());
        verify(authenticationManager).authenticate(authToken);
    }

    @Test
    public void given_user_does_not_exists_when_signin_user_service_throw_user_not_found_exception() {
        //Given
        String nonExistentUsername = "louis.mark";
        when(userRepository.findUserByUsername(nonExistentUsername)).thenReturn(null);

        //When
        UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> {
                    User user = new User();
                    user.setUsername(nonExistentUsername);
                    user.setPassword("password");
                    signinUserService.execute(user);
        });

        //Then
        Assertions.assertEquals(ErrorMessages.USER_NOT_FOUND.getMessage(), exception.getMessage());
        verify(userRepository, times(1)).findUserByUsername(nonExistentUsername);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    public void given_user_password_does_not_match_when_user_signin_service_throw_bad_credentials_exception() {
        //Given
        User user = new User();
        user.setUsername("john.doe");
        user.setPassword("im-right");

        String wrongPassword = "im-wrong";

        when(userRepository.findUserByUsername("john.doe")).thenReturn(user);
        when(passwordEncoder.matches(wrongPassword, user.getPassword())).thenReturn(false);

        //When
        BadCredentialsException exception = Assertions.assertThrows(
                BadCredentialsException.class,
                () -> {
                    User wrongUser = new User();
                    wrongUser.setUsername("john.doe");
                    wrongUser.setPassword(wrongPassword);
                    signinUserService.execute(wrongUser);
                }
        );

        //Then
        Assertions.assertEquals("Username or password invalid", exception.getMessage());
        verify(userRepository, times(1)).findUserByUsername("john.doe");
        verify(passwordEncoder, times(1)).matches(wrongPassword, user.getPassword());
    }
}
