package com.example.ProductService.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String imageUrl;
    private String category;
    private String sellerId;
    private int popularity;
}
