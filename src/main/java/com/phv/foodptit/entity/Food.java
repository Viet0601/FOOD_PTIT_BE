package com.phv.foodptit.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data 
@AllArgsConstructor
@NoArgsConstructor
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;
    private long price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(length = 1000)
    @ElementCollection
    @CollectionTable(name = "food_images", joinColumns = @JoinColumn(name = "food_id"))
    private List<String>images;
    private boolean isAvailable;
    private boolean isVegetarian;
    
    private Date createdAt;
    @JsonIgnore
    @OneToMany(mappedBy = "food",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Food_Nutrition>listNutritions=new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "food",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Food_Ingredient>listIngredients = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "food",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderItem>listOrderItems= new ArrayList<>();

    @OneToMany(mappedBy = "food",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<FoodFavorite>listFoodFavorites = new ArrayList<>();


    @JsonIgnore
    @OneToMany(mappedBy = "food",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<FootRate>listFoodRates = new  ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "food")
    private List<FoodType_Food>listTypeFoods= new ArrayList<>();
    private long shipTime;
    private long serving;
    private String star;
}
