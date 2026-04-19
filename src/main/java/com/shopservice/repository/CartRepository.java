package com.shopservice.repository;

import com.shopservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findBySessionToken(String sessionToken);
    void deleteBySessionToken(String sessionToken);
}
