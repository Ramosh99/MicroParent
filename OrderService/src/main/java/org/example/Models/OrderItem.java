package org.example.Models;

import lombok.Data;

@Data
public class OrderItem {
    private int productID;
    private int quantity;
    private double price;
}
