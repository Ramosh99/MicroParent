package com.example.ProductService.Services;

import com.example.ProductService.Dtos.AvailableRequest;
import com.example.ProductService.Dtos.ProductRequestDto;
import com.example.ProductService.Models.Product;
import com.example.ProductService.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProductServices {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequestDto productRequestDto) {
        Product product = new Product();
        product.setName(productRequestDto.getName());
        product.setPrice(productRequestDto.getPrice());
        product.setQuantity(productRequestDto.getQuantity());
        product.setDescription(productRequestDto.getDescription());
        product.setImageUrl(productRequestDto.getImageUrl());
        product.setCategory(productRequestDto.getCategory());
        product.setSellerId(productRequestDto.getSellerId());
        productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public void deleteProductById(int id) {
        productRepository.deleteById(id);
    }
    public Boolean checkAvailability(AvailableRequest availableRequest) {
        Optional<Product> product = productRepository.findById(availableRequest.getId());
        return product.isPresent() && product.get().getQuantity() >= availableRequest.getQuantity();
    }

    public void updateProductQuantity(int productId, int quantityChange) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + productId));
        if(product.getQuantity() > quantityChange) {
            int updatedQuantity = product.getQuantity() - quantityChange;
            if (updatedQuantity < 0) {
                throw new IllegalArgumentException("Insufficient stock for product ID: " + productId);
            }
            product.setQuantity(updatedQuantity);
            productRepository.save(product);
        }else {
            throw new IllegalArgumentException("Insufficient stock for product ID: " + productId);
        }

    }
}
