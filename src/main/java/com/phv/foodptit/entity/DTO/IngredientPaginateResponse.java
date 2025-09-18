package com.phv.foodptit.entity.DTO;

import java.util.List;

import com.phv.foodptit.entity.Ingredient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientPaginateResponse {
    private List<Ingredient> list;
    private long currentPage;
    private long totalPage;
}
