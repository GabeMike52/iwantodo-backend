package com.iwantodo.services.user;

import com.iwantodo.entities.user.User;
import com.iwantodo.entities.user.UserDTO;
import com.iwantodo.repositories.UserRepository;
import com.iwantodo.services.Command;
import com.iwantodo.validators.UserValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService implements Command<User, UserDTO> {
    private final UserRepository userRepository;
    public CreateUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<UserDTO> execute(User user) {
        UserValidator.execute(user);
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(new UserDTO(savedUser));
    }
}
