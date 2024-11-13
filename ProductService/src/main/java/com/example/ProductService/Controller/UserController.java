package com.example.ProductService.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/prod")
public class UserController {


    // Get User by Email
    @GetMapping()
    public String test(@RequestParam String client_id, @RequestParam String client_secret, @RequestParam String grant_type) {
        System.out.println("Received: "+client_id+" "+client_secret+" "+grant_type);
        return "clientId: "+client_id+" clientSecret: "+client_secret+" grantType: "+grant_type;
    }
}
