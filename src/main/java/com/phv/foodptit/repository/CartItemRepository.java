package com.phv.foodptit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phv.foodptit.entity.Cart;
import com.phv.foodptit.entity.CartItem;
import com.phv.foodptit.entity.Food;

import java.util.List;
import java.util.Optional;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long>{
    List<CartItem> findByCart(Cart cart);
    Optional<CartItem> findByCartAndFood(Cart cart ,Food food);
}
