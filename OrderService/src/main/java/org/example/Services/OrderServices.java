package org.example.Services;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.OrderRequestDto;
import org.example.Dtos.ProductStockRequestDto;
import org.example.Models.Order;
import org.example.Repository.OrderRepository;
import org.example.client.ProductClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServices {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public void createOrder(OrderRequestDto orderRequest) {
        Order order = Order.builder()
                .userId(orderRequest.getUserId())
                .dateCreated(orderRequest.getDateCreated())
                .items(orderRequest.getItems())
                .build();
        orderRepository.save(order);
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public String isAvailable(ProductStockRequestDto productRequest) {
        return productClient.isInStock(productRequest) ? "Product is available" : "Product Not found";
    }
}
