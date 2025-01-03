package com.iwantodo.services.user;

import com.iwantodo.entities.user.User;
import com.iwantodo.infra.exception.ErrorMessages;
import com.iwantodo.infra.exception.UserNotFoundException;
import com.iwantodo.infra.security.jwt.JwtUtil;
import com.iwantodo.repositories.UserRepository;
import com.iwantodo.services.Command;
import com.iwantodo.validators.SigninValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SigninUserService implements Command<User, String> {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(SigninUserService.class);
    private final AuthenticationManager authenticationManager;
    public SigninUserService(UserRepository userRepository,
                             PasswordEncoder encoder,
                             AuthenticationManager authenticationManager,
                             JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResponseEntity<String> execute(User user) {
        logger.info("Executing " + getClass() + " user: " + user);
        SigninValidator.execute(user);
        Optional<User> userOptional = Optional.ofNullable(userRepository.findUserByUsername(user.getUsername()));
        if(userOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        if(!encoder.matches(user.getPassword(), userOptional.get().getPassword())) {
            throw new BadCredentialsException("Username or password invalid");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtUtil.generateToken((org.springframework.security.core.userdetails.User) authentication.getPrincipal());
        return ResponseEntity.ok(jwtToken);
    }
}
