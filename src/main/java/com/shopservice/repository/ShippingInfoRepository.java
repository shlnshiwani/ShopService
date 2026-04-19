package com.shopservice.repository;

import com.shopservice.entity.ShippingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ShippingInfoRepository extends JpaRepository<ShippingInfo, Long> {
    Optional<ShippingInfo> findBySessionToken(String sessionToken);
    void deleteBySessionToken(String sessionToken);
}
