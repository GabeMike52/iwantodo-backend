package com.iwantodo.services.user;

import com.iwantodo.entities.user.User;
import com.iwantodo.entities.user.UserDTO;
import com.iwantodo.infra.exception.ErrorMessages;
import com.iwantodo.infra.exception.UserNotValidException;
import com.iwantodo.repositories.UserRepository;
import com.iwantodo.services.Command;
import com.iwantodo.validators.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateUserService implements Command<User, UserDTO> {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(CreateUserService.class);
    public CreateUserService(PasswordEncoder encoder,
                             UserRepository userRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<UserDTO> execute(User user) {
        logger.info("Executing " + getClass() + " user: " + user);
        UserValidator.execute(user);
        Optional<User> userOptional = Optional.ofNullable(userRepository.findUserByUsername(user.getUsername()));
        if(userOptional.isEmpty()) {
            User savedUser = userRepository.save(new User(user.getUsername(), user.getEmail(), encoder.encode(user.getPassword())));
            return ResponseEntity.ok(new UserDTO(savedUser));
        }
        throw new UserNotValidException(ErrorMessages.USER_ALREADY_EXISTS.getMessage());
    }
}
