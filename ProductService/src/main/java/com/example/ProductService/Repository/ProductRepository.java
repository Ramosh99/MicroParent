package com.example.ProductService.Repository;

import com.example.ProductService.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer>{

    List<Product> findByCategory(String category); //Use to retrieve product data by Product_Category
}
