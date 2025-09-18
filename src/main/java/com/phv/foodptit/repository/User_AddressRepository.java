package com.phv.foodptit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phv.foodptit.entity.Address;
import com.phv.foodptit.entity.User;
import com.phv.foodptit.entity.User_Address;
import java.util.List;

@Repository
public interface User_AddressRepository extends JpaRepository<User_Address,Long> {
    Optional<User_Address>findByCustomerAndAddress(User user,Address address);
    Optional<User_Address>deleteByCustomerAndAddress(User user,Address address);
    List<User_Address> findByCustomer(User customer);

}
