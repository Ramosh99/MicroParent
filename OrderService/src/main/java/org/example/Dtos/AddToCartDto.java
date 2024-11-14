package org.example.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartDto {
    private String CustomerEmail;
    private int productID;
    private int quantity;
    private double price;
}
