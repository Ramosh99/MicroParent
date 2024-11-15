package org.example.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private int productID;
    private String productName;
    private String productDescription;
    private int quantity;
    private double price;
    private String imageUrl;
    private String category;
}

