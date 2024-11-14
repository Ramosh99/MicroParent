package org.example.Services;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.AddToCartDto;
import org.example.Dtos.GetCartDto;
import org.example.Dtos.RemoveItemDto;
import org.example.Models.OrderItem;
import org.example.Models.ShoppingCart;
import org.example.Repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServices {
    final ShoppingCartRepository shoppingCartRepository;

    public void addToCart(AddToCartDto addToCartDto) {
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findByCustomerEmail(addToCartDto.getCustomerEmail());
        ShoppingCart shoppingCart;
        if (optionalCart.isPresent()) {
            shoppingCart = optionalCart.get();

            OrderItem newItem = OrderItem.builder()
                    .productID(addToCartDto.getProductID())
                    .quantity(addToCartDto.getQuantity())
                    .price(addToCartDto.getPrice())
                    .build();

            List<OrderItem> existingItems = shoppingCart.getOrderItems();
            if (existingItems == null) {
                existingItems = new ArrayList<>();
            }

            existingItems.add(newItem);
            shoppingCart.setOrderItems(existingItems);

        }
        else {
            OrderItem newItem = OrderItem.builder()
                    .productID(addToCartDto.getProductID())
                    .quantity(addToCartDto.getQuantity())
                    .price(addToCartDto.getPrice())
                    .build();

            List<OrderItem> items = new ArrayList<>();
            items.add(newItem);

            shoppingCart = ShoppingCart.builder()
                    .customerEmail(addToCartDto.getCustomerEmail())
                    .orderItems(items)
                    .build();
        }
        shoppingCartRepository.save(shoppingCart);
    }

    public ShoppingCart getCart(GetCartDto getCartDto) {
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findByCustomerEmail(getCartDto.getCustomerEmail());
        return optionalCart.orElse(null);
    }

    public boolean removeItemFromCart(RemoveItemDto removeItemDto) {
        System.out.println("Email: " + removeItemDto.getCustomerEmail());
        ShoppingCart shoppingCart;
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findByCustomerEmail(removeItemDto.getCustomerEmail());
        if (optionalCart.isPresent()) {
            shoppingCart = optionalCart.get();
            List<OrderItem> orderItems = shoppingCart.getOrderItems();
            boolean removed = orderItems.removeIf(item -> item.getProductID() == removeItemDto.getItemID());

            if (removed) {
                shoppingCartRepository.save(shoppingCart);
                return true;
            }
        }
        return false;
    }
}
