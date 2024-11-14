package org.example.Repository;

import org.example.Models.ShoppingCart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends MongoRepository<ShoppingCart, String> {
    Optional<ShoppingCart> findByCustomerEmail(String customerEmail);
}
