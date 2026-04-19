package com.shopservice.repository;

import com.shopservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findBySessionTokenOrderByPlacedAtAsc(String sessionToken);
}
