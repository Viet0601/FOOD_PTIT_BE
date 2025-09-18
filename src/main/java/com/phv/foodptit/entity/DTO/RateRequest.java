package com.phv.foodptit.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateRequest {
    private long foodId;
    private long rating;
    private String comment;
}
