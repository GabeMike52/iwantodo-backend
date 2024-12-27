package com.iwantodo.services.user;

import com.iwantodo.entities.user.User;
import com.iwantodo.infra.exception.ErrorMessages;
import com.iwantodo.infra.exception.UserNotFoundException;
import com.iwantodo.repositories.UserRepository;
import com.iwantodo.services.Command;
import com.iwantodo.validators.SigninValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SigninUserService implements Command<User, String> {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private static final Logger logger = LoggerFactory.getLogger(SigninUserService.class);
    public SigninUserService(UserRepository userRepository,
                             PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public ResponseEntity<String> execute(User user) {
        logger.info("Executing " + getClass() + " user: " + user);
        SigninValidator.execute(user);
        Optional<User> userOptional = Optional.ofNullable(userRepository.findUserByUsername(user.getUsername()));
        if(userOptional.isEmpty()) {
            throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND.getMessage());
        }
        if(!encoder.matches(user.getPassword(), userOptional.get().getPassword())) {
            throw new BadCredentialsException("Username or password invalid");
        }
        return ResponseEntity.ok("User successfully signed-in");
    }
}
