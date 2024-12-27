package com.iwantodo.controllers;

import com.iwantodo.entities.user.User;
import com.iwantodo.entities.user.UserDTO;
import com.iwantodo.services.user.CreateUserService;
import com.iwantodo.services.user.SigninUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final CreateUserService createUserService;
    private final SigninUserService signinUserService;
    public UserController(CreateUserService createUserService,
                          SigninUserService signinUserService) {
        this.createUserService = createUserService;
        this.signinUserService = signinUserService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        return createUserService.execute(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signInUser(@RequestBody User user) {
        return signinUserService.execute(user);
    }
}
