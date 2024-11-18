package org.example.Controllers;

import org.example.DTO.EditUser;
import org.example.DTO.LoginRequest;
import org.example.DTO.SignUpRequest;
import org.example.Exceptions.AlreadyExistsException;
import org.example.Exceptions.InvalidFormatException;
import org.example.Exceptions.UnauthorizedException;
import org.example.Models.User;
import org.example.Services.AuthService;
import org.example.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public Mono<ResponseEntity<Object>> createUser(@RequestBody SignUpRequest user) {
        return userService.createUser(user)
                .map(userOptional -> userOptional
                        .<ResponseEntity<Object>>map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"))
                )
                .onErrorResume(e -> {
                    if (e instanceof AlreadyExistsException) {
                        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists"));
                    } else if (e instanceof InvalidFormatException) {
                        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()));
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error"));
                    }
                });
    }


    @PostMapping("/login")
    public Mono<ResponseEntity<String>> logUser(@RequestBody LoginRequest loginData) {
        return authService.logUser(loginData.email, loginData.password)
                .map(ResponseEntity::ok)
                .onErrorResume(ex -> {
                    if (ex instanceof UnauthorizedException) {
                        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"));
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error"));
                    }
                });
    }


    // Update User
    @PutMapping("/{email}")
    public ResponseEntity<User> updateUser(@PathVariable String email, @RequestBody EditUser userDetails) {
        try {
            User updatedUser = userService.updateUser(email, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            System.out.println("Error: "+e);
            return ResponseEntity.notFound().build();
        }
    }

}
