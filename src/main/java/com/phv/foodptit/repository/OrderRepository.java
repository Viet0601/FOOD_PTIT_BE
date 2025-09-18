package com.phv.foodptit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phv.foodptit.entity.Order;
import com.phv.foodptit.entity.User;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Page<Order> findByCustomer(User customer,Pageable pageable);
}
