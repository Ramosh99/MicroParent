package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.AddToCartDto;
import org.example.Services.ShoppingCartServices;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartServices shoppingCartServices;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void AddToCart(@RequestBody AddToCartDto addToCartDto) {
        shoppingCartServices.addToCart(addToCartDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public void GetCart(){}

}
