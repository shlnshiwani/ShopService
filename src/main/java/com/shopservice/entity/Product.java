package com.shopservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    private String img;
    private String category;

    @Column(length = 1000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "product_specs", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "spec")
    @OrderColumn(name = "spec_order")
    @Builder.Default
    private List<String> specs = new ArrayList<>();
}
