package com.phv.foodptit.entity.DTO;

import java.util.List;

import com.phv.foodptit.entity.Food;
import com.phv.foodptit.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String email;
    private String fullName;
   private Role role;
   private List<Food>favoriteFoods;
}
