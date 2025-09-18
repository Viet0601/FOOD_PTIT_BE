package com.phv.foodptit.entity.DTO;

import java.util.List;

import com.phv.foodptit.entity.Food;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodPaginateResponse {
    private List<Food>listFood;
    private long totalPage;
    private long currentPage;
}
