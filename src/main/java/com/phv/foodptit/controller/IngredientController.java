package com.phv.foodptit.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phv.foodptit.entity.DTO.AddIngredientRequest;
import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.service.IngredientService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class IngredientController {
    private final IngredientService ingredientService;
    @PostMapping("/admin/ingredient")
    public DataResponse addNewIngredient(@RequestBody AddIngredientRequest request){
        return this.ingredientService.addNewIngredient(request);
    }
    @PutMapping("/admin/ingredient/{id}")
    public DataResponse updateIngredient(@RequestBody AddIngredientRequest request,@PathVariable String id){
        return this.ingredientService.updateIngredient(request,Long.parseLong(id));
    }
    @GetMapping("/ingredient")
    public DataResponse getAllIngredient(@RequestParam(required = false) String page,@RequestParam(required = false) String limit){
        if(page !=null && limit!=null)
        {
            Pageable pageable= PageRequest.of(Integer.parseInt(page)-1, Integer.parseInt(limit),Sort.by(Sort.Direction.DESC, "id"));
             return this.ingredientService.getAllIngredient(pageable);
        }
        else 
        {
            return this.ingredientService.getAllIngredient(null);
        }
       
    }
    
}
