package org.example.Models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(value = "shopping-cart")
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCart {
    @Id
     private String cartId;
     private String customerEmail;
     private List<OrderItem> orderItems;
}
