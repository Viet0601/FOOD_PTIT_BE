package com.phv.foodptit.entity;

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
@Entity
@Table(name = "food_nutrition")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Food_Nutrition {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY )
    private long id;
    private String amount;
    @ManyToOne
    @JoinColumn(name="food_id")
    private Food food;
    @ManyToOne
    @JoinColumn(name = "nutrition_id")
    private Nutrition nutrition;
}

