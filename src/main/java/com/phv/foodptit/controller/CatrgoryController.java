package com.phv.foodptit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController

@RequiredArgsConstructor
public class CatrgoryController {
    private final CategoryService categoryService;
    @GetMapping("/category")
    public DataResponse getAllCategory(){
        return this.categoryService.getAllCategory();
    }
}
