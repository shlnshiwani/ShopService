package com.shopservice.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private String reviewerName;
    private String comment;
    private Integer rating;
}
