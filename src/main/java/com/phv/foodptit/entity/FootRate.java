package com.phv.foodptit.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "food_rates")
public class FootRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="user_id")
    private User customer;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="food_id")
    private Food food;

    private long star;
    private String feedback;
    private Date createdAt;
}
