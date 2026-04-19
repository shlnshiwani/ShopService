package com.shopservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    private String username;

    private String createdAt;
    private String expiresAt;

    @Builder.Default
    private Boolean rememberMe = false;
}
