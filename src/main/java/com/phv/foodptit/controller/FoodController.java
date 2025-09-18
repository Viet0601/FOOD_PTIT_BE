package com.phv.foodptit.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.entity.DTO.FoodRequest;
import com.phv.foodptit.entity.DTO.IngredientRequest;
import com.phv.foodptit.entity.DTO.NutritionRequest;
import com.phv.foodptit.service.CloudinaryService;
import com.phv.foodptit.service.FoodService;

import lombok.RequiredArgsConstructor;

@RestController

@RequiredArgsConstructor
public class FoodController {
    
    private final CloudinaryService cloudinaryService;
    private final FoodService foodService;
    @PostMapping("/api/v1/admin/food")
    public DataResponse createNewFood(@RequestParam("files") MultipartFile[] files,@RequestParam String ingredients,@RequestParam String nutritions,  @ModelAttribute FoodRequest foodRequest ) throws IOException {
       
         ObjectMapper mapper = new ObjectMapper();
        List<IngredientRequest> listIngredients = Arrays.asList(mapper.readValue(ingredients, IngredientRequest[].class));
        List<NutritionRequest> listNutritions = Arrays.asList(mapper.readValue(nutritions, NutritionRequest[].class));
         List<String> listImage= this.cloudinaryService.uploadMultipleFiles(files);
        DataResponse dataResponse= this.foodService.createNewFood(listImage, foodRequest,listIngredients,listNutritions);
        return  dataResponse;
    }
    @PutMapping("/api/v1/admin/food/{id}")
    public DataResponse updateFood(@RequestParam("files" ) MultipartFile[] files,@ModelAttribute FoodRequest foodRequest,@PathVariable long id, @RequestParam String ingredients,@RequestParam String nutritions)throws IOException{
        List<String>images= this.cloudinaryService.uploadMultipleFiles(files);
         ObjectMapper mapper = new ObjectMapper();
        List<NutritionRequest> listNutritions = Arrays.asList(mapper.readValue(nutritions, NutritionRequest[].class));
         List<IngredientRequest> listIngredients = Arrays.asList(mapper.readValue(ingredients, IngredientRequest[].class));
        DataResponse dataResponse= this.foodService.updateFood(images, foodRequest, id,listIngredients,listNutritions);

        return dataResponse;
    }
    @DeleteMapping("/api/v1/admin/food/{id}")
    public DataResponse deleteById(@PathVariable long id){
        DataResponse response= this.foodService.deteleFood(id);
        return response;
    }
    @GetMapping("/food")
    public DataResponse getAllFood(@RequestParam(required = false) String page,@RequestParam(required = false) String limit,@RequestParam(required = false) String category){
       if(page!=null && limit!=null){
         Pageable pageable= PageRequest.of(Integer.parseInt(page)-1, Integer.parseInt(limit));
          DataResponse dataResponse= this.foodService.getALlFood(pageable,category);
        return dataResponse;
        }
       else 
       {
        DataResponse dataResponse=this.foodService.getALlFood(null,category);
      return dataResponse;
    }
    }
   @GetMapping("/food/search")
public DataResponse searchFood(@RequestParam String query) {
    return this.foodService.SearchFood(query);
}
   @GetMapping("/food/category/{id}")
public DataResponse searchFoodByCategory(@PathVariable String id) {
    return this.foodService.getFoodByCategory(Long.parseLong(id));
}
    @PutMapping("/api/v1/food/add-favorite/{id}")
    public DataResponse addFoodFavorite(@CurrentSecurityContext(expression = "authentication?.name")String email,@PathVariable String id){
        return this.foodService.addFoodFavorite(email, Long.parseLong(id));
    }
    @GetMapping("food/{id}")
    public DataResponse getFoodById(@PathVariable String id){
        return this.foodService.getFoodById(Long.parseLong(id));
    }
}
