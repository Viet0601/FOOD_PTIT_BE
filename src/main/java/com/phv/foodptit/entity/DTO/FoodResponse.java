package com.phv.foodptit.entity.DTO;

import java.util.List;

import com.phv.foodptit.entity.Category;
import com.phv.foodptit.entity.FootRate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodResponse {
    private FeedbackResponse reviewCustomers;
    private Category category;
    private List<String>listImages;
    private List<IngredientResponse>listIngredients;
    private List<NutritionResponse>listNutritions;
    private long id;

    private String name;
    private String description;
    private long price;
    private boolean available;
    private boolean vegetarian;
    private long serving;
    private long shipTime;
    private String star;
    private long feedbacks;
}
