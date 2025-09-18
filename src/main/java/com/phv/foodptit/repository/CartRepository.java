package com.phv.foodptit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phv.foodptit.entity.Cart;
import com.phv.foodptit.entity.User;

import java.util.List;
import java.util.Optional;


public interface CartRepository extends JpaRepository<Cart,Long> {
     Optional<Cart> findByCustomer(User customer);;
}

