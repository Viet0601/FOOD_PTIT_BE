package com.phv.foodptit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phv.foodptit.entity.Role;
import java.util.List;
import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String name);
}
