package com.phv.foodptit.entity.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {
    private List<FoodFeedbackResponse>listFeedbacks;
    private long totalFeedbacks;
}
