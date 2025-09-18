package com.phv.foodptit.entity.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodFeedbackResponse {
    private String name;
    private Date createdAt;
    private long star;
    private String feedback;
    
}
