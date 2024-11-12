package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.OrderRequestDto;
import org.example.Dtos.ProductStockRequestDto;
import org.example.Models.Order;
import org.example.Services.OrderServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServices orderServices;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void placeOrder(@RequestBody OrderRequestDto orderRequestDto) {
        orderServices.createOrder(orderRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public List<Order> getOrders() {
        return orderServices.getOrders();
    }

    //check whether product is available
    @PostMapping("/availability")
    public ResponseEntity<String> getAvailability(@RequestBody ProductStockRequestDto productStockRequestDto) {
        String availability = orderServices.isAvailable(productStockRequestDto);
        return new ResponseEntity<>(availability, HttpStatus.OK);
    }
}
