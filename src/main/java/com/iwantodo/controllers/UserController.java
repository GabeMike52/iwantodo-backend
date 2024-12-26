package com.iwantodo.controllers;

import com.iwantodo.entities.user.User;
import com.iwantodo.entities.user.UserDTO;
import com.iwantodo.services.user.CreateUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final CreateUserService createUserService;
    public UserController(CreateUserService createUserService) {
        this.createUserService = createUserService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        return createUserService.execute(user);
    }
}
