package com.shopservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    private String id;   // ORD-XXXXXX

    @Column(nullable = false)
    private String sessionToken;

    private Double total;
    private String placedAt;

    // Shipping snapshot (embedded so it survives shipping record deletion)
    private String shippingName;
    private String shippingEmail;
    private String shippingAddress;
    private String shippingCity;
    private String shippingZip;
    private String shippingCountry;
    private String shippingDeliveryDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
}
