package com.phv.foodptit.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private long id;
    private String phone;
    private String village;
    private String district;
    private String province;
    private String detail;
    @JsonIgnore
    @OneToMany(mappedBy = "address",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<User_Address>listAddresses;

    @JsonIgnore
    @OneToMany(mappedBy = "address")
    private List<Order>listOrders;
}
