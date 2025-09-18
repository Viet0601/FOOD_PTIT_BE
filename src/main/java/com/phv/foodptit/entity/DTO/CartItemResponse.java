package com.phv.foodptit.entity.DTO;

import com.phv.foodptit.entity.Food;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private long id;
    private Food food;
    private long quantity;
    private long totalPrice;
}
