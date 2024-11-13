package com.example.ProductService.Controller;

import com.example.ProductService.Dtos.AvailableRequest;
import com.example.ProductService.Dtos.ProductRequestDto;
import com.example.ProductService.Dtos.QuantityRequest;
import com.example.ProductService.Models.Product;
import com.example.ProductService.Services.ProductServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value ="api/v1/product" )
@CrossOrigin
@RequiredArgsConstructor
public class ProductController {
    private final ProductServices productServices;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequestDto productRequest){
        productServices.createProduct(productRequest);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllProducts(){
        return productServices.getAllProducts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product getProduct(@PathVariable int id){
        return productServices.getProductById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(@PathVariable int id){
        productServices.deleteProductById(id);
    }

    @PostMapping("/availability")
    @ResponseStatus(HttpStatus.OK)
    public boolean isProductAvailable(@RequestBody AvailableRequest productRequest){
        return productServices.checkAvailability(productRequest);
    }

    @PostMapping("/quantity/update")
    @ResponseStatus(HttpStatus.OK)
    public void changeProductQuantity(@RequestBody QuantityRequest quantityRequest) {
        productServices.updateProductQuantity(quantityRequest.getId(), quantityRequest.getQuantity());
    }
}
