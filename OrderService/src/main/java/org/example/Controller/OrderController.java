package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.OrderDto;
import org.example.Dtos.OrderRequestDto;
import org.example.Dtos.ProductStockRequestDto;
import org.example.Dtos.QuantityRequest;
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
        orderServices.placeOrder(orderRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public List<Order> getOrders() {
        return orderServices.getOrders();
    }

    // create simple order
    @PostMapping("create-order")
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestBody OrderDto orderRequestDto) {
        orderServices.createOrder(orderRequestDto);
    }

    //check whether product is available
    @PostMapping("/availability")
    public ResponseEntity<String> getAvailability(@RequestBody ProductStockRequestDto productStockRequestDto) {
        String availability = orderServices.isAvailable(productStockRequestDto);
        return new ResponseEntity<>(availability, HttpStatus.OK);
    }

    //update product quantity when place order
    @PostMapping("/quantity/update")
    @ResponseStatus(HttpStatus.OK)
    public void changeQuantity(@RequestBody QuantityRequest quantityRequest) {
        orderServices.updateProductQuantity(quantityRequest);
    }
}
