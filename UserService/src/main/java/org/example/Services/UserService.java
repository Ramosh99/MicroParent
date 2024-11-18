package org.example.Services;

import org.example.DTO.Convertion.SignUptoUser;
import org.example.DTO.EditUser;
import org.example.DTO.SignUpRequest;
import org.example.Exceptions.AlreadyExistsException;
import org.example.Exceptions.InvalidFormatException;
import org.example.Models.User;
import org.example.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;

    // Create User
    public Mono<Optional<User>> createUser(SignUpRequest user) {
        return authService.addUser(user.email, user.password, user.firstName, user.lastName, user.role)
                .flatMap(result -> {
                    try {
                        User savedUser = userRepository.save(SignUptoUser.convert(user));
                        return Mono.just(Optional.of(savedUser));
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Unexpected error", e));
                    }
                })
                .onErrorResume(e -> {
                    if (e instanceof AlreadyExistsException) {
                        return Mono.error(e);
                    } else if (e instanceof InvalidFormatException) {
                        return Mono.error(new InvalidFormatException(e.getMessage()));
                    } else {
                        return Mono.error(new RuntimeException("Unexpected error"));
                    }
                })
                .defaultIfEmpty(Optional.empty());
    }

    // Get User by Email
    public Optional<User> getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findById(email);
        System.out.println("userOptional: "+userOptional);
        return userOptional;
    }

    // Get All Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Update User
    public User updateUser(String email, EditUser userDetails) {
        return userRepository.findById(email).map(user -> {
            user.setPhoneNumber(userDetails.phoneNumber);
            user.setFirstName(userDetails.firstName);
            user.setLastName(userDetails.lastName);
            user.setAddressLine1(userDetails.addressLine1);
            user.setAddressLine2(userDetails.addressLine2);
            user.setAddressLine3(userDetails.addressLine3);
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    // Delete User
    public void deleteUser(String email) {
        userRepository.deleteById(email);
    }
}
