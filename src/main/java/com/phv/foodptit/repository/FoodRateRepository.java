package com.phv.foodptit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phv.foodptit.entity.FootRate;

@Repository
public interface FoodRateRepository extends JpaRepository<FootRate,Long>{
    
}
