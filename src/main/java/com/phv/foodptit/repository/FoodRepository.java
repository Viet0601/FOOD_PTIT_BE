package com.phv.foodptit.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.phv.foodptit.entity.Category;
import com.phv.foodptit.entity.Food;

public interface FoodRepository extends JpaRepository<Food,Long> {
   @Query(value = "SELECT * FROM food f " +
               "WHERE f.name LIKE CONCAT('%', :query, '%')",
       nativeQuery = true)
    List<Food> findBySearchQuery(String query);
    List<Food> findByCategory( Category category);
    Page<Food> findByCategory(Category category,Pageable pageable);
}
