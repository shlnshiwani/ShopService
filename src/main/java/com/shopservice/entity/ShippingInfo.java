package com.shopservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shipping_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String sessionToken;

    private String name;
    private String email;
    private String address;
    private String city;
    private String zip;
    private String country;
    private String deliveryDate;
}
