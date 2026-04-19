package com.shopservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "faqs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Faq {

    @Id
    private Integer id;

    @Column(length = 500)
    private String question;

    @Column(length = 2000)
    private String answer;
}
