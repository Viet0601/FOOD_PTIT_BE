package com.phv.foodptit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.service.NutritrionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NutritrionController {
    private final NutritrionService nutritrionService;

    @GetMapping("/nutrition")
    public DataResponse getAllNutrition(){
        return this.nutritrionService.getAllNutritrion();
    }
}
