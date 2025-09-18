package com.phv.foodptit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phv.foodptit.entity.Food_Ingredient;
import com.phv.foodptit.entity.Food;


@Repository
public interface Food_IngredientRepository extends JpaRepository<Food_Ingredient,Long> {
    void deleteAllByFood(Food food);
}
