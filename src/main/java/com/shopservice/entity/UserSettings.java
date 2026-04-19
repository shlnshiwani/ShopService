package com.shopservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Builder.Default private Boolean emailNotifications = true;
    @Builder.Default private Boolean smsNotifications = false;
    @Builder.Default private String theme = "light";
    @Builder.Default private String currency = "USD";
    @Builder.Default private Integer itemsPerPage = 4;
}
