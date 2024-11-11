package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.DTO.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String placeOrder(@RequestBody String data) {
        return data+" auth success";
    }

    @PostMapping("signup")
    @ResponseStatus(HttpStatus.OK)
    public String signup(@RequestBody String data) {
        return data+" signup success";
    }

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public String login(@RequestBody Auth data) {
        return data.username+" login success with "+data.password;
    }
}
