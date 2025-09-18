package com.phv.foodptit.service;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.phv.foodptit.entity.Category;
import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.repository.CategoryRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public DataResponse getAllCategory(){
        try {
             List<Category>list= this.categoryRepository.findAll();
             return new DataResponse(HttpStatus.SC_OK,"",list);
        } catch (Exception e) {
            // TODO: handle exception
            return new DataResponse(HttpStatus.SC_BAD_REQUEST,null,null);
        }
       
        
    }
}
