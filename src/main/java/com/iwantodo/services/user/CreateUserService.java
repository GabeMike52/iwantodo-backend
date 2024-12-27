package com.iwantodo.services.user;

import com.iwantodo.entities.user.User;
import com.iwantodo.entities.user.UserDTO;
import com.iwantodo.repositories.UserRepository;
import com.iwantodo.services.Command;
import com.iwantodo.validators.UserValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateUserService implements Command<User, UserDTO> {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    public CreateUserService(PasswordEncoder encoder,
                             UserRepository userRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<UserDTO> execute(User user) {
        UserValidator.execute(user);
        Optional<User> userOptional = Optional.ofNullable(userRepository.findUserByUsername(user.getUsername()));
        if(userOptional.isEmpty()) {
            User savedUser = userRepository.save(new User(user.getUsername(), user.getEmail(), encoder.encode(user.getPassword())));
            return ResponseEntity.ok(new UserDTO(savedUser));
        }
        throw new RuntimeException("User already exists");
    }
}
