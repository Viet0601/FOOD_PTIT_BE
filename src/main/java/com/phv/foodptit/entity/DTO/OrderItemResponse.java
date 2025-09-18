package com.phv.foodptit.entity.DTO;

import java.util.List;

import com.phv.foodptit.entity.Address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private long foodId;
    private String name;
    private long quantity;
    private long total;
    private List<String> images;
   
}
