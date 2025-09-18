package com.phv.foodptit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phv.foodptit.entity.StatusOrder;
import java.util.List;
import java.util.Optional;


@Repository
public interface StatusOrderRepository extends JpaRepository<StatusOrder,Long> {
    Optional<StatusOrder> findByDescription(String description);
}
