package com.phv.foodptit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phv.foodptit.entity.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    
}
