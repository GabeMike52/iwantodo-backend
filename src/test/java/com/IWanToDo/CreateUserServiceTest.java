package com.IWanToDo;

import com.iwantodo.entities.user.User;
import com.iwantodo.entities.user.UserDTO;
import com.iwantodo.repositories.UserRepository;
import com.iwantodo.services.user.CreateUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

public class CreateUserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserService createUserService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void given_user_information_when_create_user_service_then_return_user_dto() {
        //Given
        User user = new User();
        user.setId(1L);
        user.setUsername("John Doe");
        user.setEmail("john@doe.com");
        user.setPassword("TestingTestTestable");

        User userEncoded = new User();
        userEncoded.setId(1L);
        userEncoded.setUsername("John Doe");
        userEncoded.setEmail("john@doe.com");
        userEncoded.setPassword("encoded-password");

        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(null);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(userEncoded);

        //When
        ResponseEntity<UserDTO> response = createUserService.execute(user);

        //Then
        Assertions.assertEquals(new UserDTO(userEncoded), response.getBody());
        verify(userRepository, times(1)).findUserByUsername(user.getUsername());
        verify(passwordEncoder, times(1)).encode(user.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }
}
