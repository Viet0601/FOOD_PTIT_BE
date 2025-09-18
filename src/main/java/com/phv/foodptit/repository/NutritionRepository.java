package com.phv.foodptit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phv.foodptit.entity.Nutrition;

@Repository
public interface NutritionRepository extends JpaRepository<Nutrition,Long> {
    
}
