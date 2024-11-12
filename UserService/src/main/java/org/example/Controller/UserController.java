package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.DTO.SignUpRequest;
import org.example.Services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final AuthService authService;

    @PostMapping("new")
    @ResponseStatus(HttpStatus.OK)
    public String auth(@RequestBody String data) {
        return data+" auth success";
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) {
        authService.createUser(signUpRequest.Username, signUpRequest.Password, signUpRequest.Role);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }
}
