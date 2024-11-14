package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.AddToCartDto;
import org.example.Dtos.GetCartDto;
import org.example.Dtos.RemoveItemDto;
import org.example.Models.ShoppingCart;
import org.example.Services.ShoppingCartServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ShoppingCart GetCart(@RequestBody GetCartDto getCartDto) {
        return shoppingCartServices.getCart(getCartDto);
    }

    @DeleteMapping
    public ResponseEntity<String> RemoveItem(@RequestBody RemoveItemDto removeItemDto){
        boolean isRemoved = shoppingCartServices.removeItemFromCart(removeItemDto);

        if (isRemoved) {
            return ResponseEntity.ok("Item removed from cart successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found in cart or cart does not exist");
        }
    }

}
