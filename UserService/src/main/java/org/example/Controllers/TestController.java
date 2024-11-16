package org.example.Controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("seller")
    @PreAuthorize("hasRole('SELLER')")
    public String testSeller() {
        return "Test passed";
    }

    @GetMapping("buyer")
    @PreAuthorize("hasRole('BUYER')")
    public String testBuyer() {
        return "Test passed";
    }
}
