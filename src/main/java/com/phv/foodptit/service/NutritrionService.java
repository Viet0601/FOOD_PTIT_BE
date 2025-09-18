package com.phv.foodptit.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.phv.foodptit.entity.Nutrition;
import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.repository.NutritionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NutritrionService {
    private final NutritionRepository nutritionRepository;

    public DataResponse getAllNutritrion(){
        try {
            List<Nutrition>list = this.nutritionRepository.findAll();
            return new DataResponse(HttpStatus.OK.value(), null, list);
        } catch (Exception e) {
            // TODO: handle exception
            return new DataResponse(HttpStatus.BAD_REQUEST.value(), "Đã xảy ra lỗi!", null);
        }
    }
}
