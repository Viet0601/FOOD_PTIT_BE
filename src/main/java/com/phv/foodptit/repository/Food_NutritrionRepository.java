package com.phv.foodptit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phv.foodptit.entity.Food_Nutrition;

@Repository
public interface Food_NutritrionRepository extends JpaRepository<Food_Nutrition,Long> {
    
}
