package com.shopservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReorderRequest {
    private List<String> ids;   // product ids in new order
}
