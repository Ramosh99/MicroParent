package org.example.Controllers;

import org.example.DTO.LoginRequest;
import org.example.Exceptions.UnauthorizedException;
import org.example.Models.User;
import org.example.Services.AuthService;
import org.example.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public Mono<ResponseEntity<User>> createUser(@RequestBody User user) {
        return userService.createUser(user)
                .map(userOptional -> userOptional
                        .map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build())
                );
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> logUser(@RequestBody LoginRequest loginData) {
        return authService.logUser(loginData.username, loginData.password)
                .map(ResponseEntity::ok)
                .onErrorResume(ex -> {
                    if (ex instanceof UnauthorizedException) {
                        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"));
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error"));
                    }
                });
    }

}
