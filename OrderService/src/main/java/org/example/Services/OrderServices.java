package org.example.Services;

import lombok.RequiredArgsConstructor;
import org.example.Dtos.*;
import org.example.Models.Order;
import org.example.Models.OrderItem;
import org.example.Repository.OrderRepository;
import org.example.client.ProductClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.example.client.Product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServices {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;


    //post order
    public void createOrder(OrderDto orderRequest) {
        Order order = Order.builder()
                .userId(orderRequest.getUserId())
                .dateCreated(orderRequest.getDateCreated())
                .items(orderRequest.getItems())
                .build();
        orderRepository.save(order);
    }

    //place order with check availability and update quantity
    public void placeOrder(OrderRequestDto orderRequest) {
        // Check availability for each item
        for (OrderRequestDto.Item item : orderRequest.getItems()) {
            ProductStockRequestDto stockRequest = new ProductStockRequestDto();
            stockRequest.setId(item.getProductID());
            stockRequest.setQuantity(item.getQuantity());

            if (!isProductAvailable(stockRequest)) {
                throw new RuntimeException("Product is out of stock " + item.getProductID());
            }
        }
        // If all items are available, place the order
        Order order = Order.builder()
                .userId(orderRequest.getUserId())
                .dateCreated(orderRequest.getDateCreated())
                .items(orderRequest.getItems().stream()
                        .map(item -> new OrderItem(item.getProductID(), item.getQuantity(), item.getPrice()))
                        .collect(Collectors.toList())
                )
                .build();
        orderRepository.save(order);


        // Update quantity for each product in the order
        for (OrderRequestDto.Item item : orderRequest.getItems()) {
            QuantityRequest quantityRequest = new QuantityRequest();
            quantityRequest.setId(item.getProductID());
            quantityRequest.setQuantity(item.getQuantity());
            updateProductQuantity(quantityRequest);
        }
    }


    public List<Order> getOrders() {
        return orderRepository.findAll();
    }


    public String isAvailable(ProductStockRequestDto productRequest) {
        return productClient.isInStock(productRequest) ? "Product is available" : "Product Not found";
    }

    private boolean isProductAvailable(ProductStockRequestDto productRequest) {
        return productClient.isInStock(productRequest);
    }

    public void updateProductQuantity(QuantityRequest quantityRequest) {
        productClient.updateQuantity(quantityRequest);
    }

    public boolean updateOrderStatus(String orderId, String status) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(status); // Set the new status
            orderRepository.save(order); // Save the updated order
            return true;
        }
        return false;
    }

    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    // Fetch orders by user ID
    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    // Fetch full order details by order ID including product details
    public OrderDetailsDto getFullOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        // Fetch detailed product information for each order item
        List<OrderItemDto> orderItemDtos = order.getItems().stream().map(orderItem -> {
            Product product = productClient.getProductById(orderItem.getProductID());
            return new OrderItemDto(orderItem.getProductID(), product.getName(), product.getDescription(),
                    orderItem.getQuantity(), product.getPrice(), product.getImageUrl(), product.getCategory());
        }).collect(Collectors.toList());

        // Build and return detailed order information
        return OrderDetailsDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .dateCreated(order.getDateCreated())
                .status(order.getStatus())
                .items(orderItemDtos)
                .build();
    }

}
