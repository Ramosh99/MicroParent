package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.*;
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

    // update order status via url
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable String id, @RequestParam String status) {
        boolean updated = orderServices.updateOrderStatus(id, status);
        if (updated) {
            return ResponseEntity.ok("Order status updated successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // update order status via json body
    @PutMapping("/update-status")
    public ResponseEntity<String> updateOrderStatus(@RequestBody UpdateOrderStatusRequest request) {
        boolean updated = orderServices.updateOrderStatus(request.getId(), request.getStatus());
        if (updated) {
            return ResponseEntity.ok("Order status updated successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // get order details by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        Order order = orderServices.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
