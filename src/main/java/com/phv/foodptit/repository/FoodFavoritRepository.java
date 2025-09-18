package com.phv.foodptit.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phv.foodptit.entity.Food;
import com.phv.foodptit.entity.FoodFavorite;
import com.phv.foodptit.entity.User;

@Repository
public interface FoodFavoritRepository extends JpaRepository<FoodFavorite,Long> {
    Optional<FoodFavorite> findByCustomerAndFood(User user,Food food);
}
