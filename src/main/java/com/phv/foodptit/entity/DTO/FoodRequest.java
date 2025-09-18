package com.phv.foodptit.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodRequest {
     private String name;
    private String description;
    private long price;
     private boolean available;   
    private boolean vegetarian;
    private long category_id;
    private long serving;
    private long shipTime;
    
}
