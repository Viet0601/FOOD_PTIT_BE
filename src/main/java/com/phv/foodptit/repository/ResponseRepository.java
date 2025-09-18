package com.phv.foodptit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phv.foodptit.entity.Response;

@Repository
public interface ResponseRepository extends JpaRepository<Response,Long> {
    
}
