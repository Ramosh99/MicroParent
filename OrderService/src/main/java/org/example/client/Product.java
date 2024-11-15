package org.example.client;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String imageUrl;
    private String category;
    private String sellerId;
}

