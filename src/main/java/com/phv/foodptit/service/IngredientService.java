package com.phv.foodptit.service;

import java.util.List;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phv.foodptit.entity.Ingredient;
import com.phv.foodptit.entity.DTO.AddIngredientRequest;
import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.entity.DTO.IngredientPaginateResponse;
import com.phv.foodptit.repository.IngredientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    @Transactional
    public DataResponse addNewIngredient(AddIngredientRequest request){
      
            Ingredient ingredient=new Ingredient();
            Optional<Ingredient> isExist= this.ingredientRepository.findByName(request.getName().toLowerCase());
            if(isExist.isPresent())
            {
                throw new RuntimeException("Đã tồn tại nguyên liệu này!");
            }
          

            ingredient.setName(request.getName().toLowerCase());
            ingredient.setUnit(request.getUnit());

            this.ingredientRepository.save(ingredient);
            return new DataResponse(HttpStatus.SC_CREATED, "Thêm mới nguyên liệu thành công", ingredient);
      
    }
    @Transactional
    public DataResponse updateIngredient(AddIngredientRequest request,long id){
       
            Ingredient ingredient=this.ingredientRepository.findById(id)
            .orElseThrow(()->new RuntimeException("Không tìm thấy nguyên liệu"));
            ingredient.setName(request.getName());
            ingredient.setUnit(request.getUnit());
            this.ingredientRepository.save(ingredient);
            return new DataResponse(HttpStatus.SC_OK, "Cập nhật nguyên liệu thành công!", ingredient);
       
    }
    public DataResponse getAllIngredient(Pageable pageable){
        try {
            if(pageable!=null)
            {
                  Page<Ingredient> pages=this.ingredientRepository.findAll(pageable);
            return new DataResponse(HttpStatus.SC_OK, null, new IngredientPaginateResponse(pages.getContent(),pages.getNumber()+1,pages.getTotalPages()));
            }
            else 
            {
                  List<Ingredient>list=this.ingredientRepository.findAll();
            return new DataResponse(HttpStatus.SC_OK, null, list);
            }
          
        } catch (Exception e) {
            // TODO: handle exception
            return new DataResponse(HttpStatus.SC_BAD_REQUEST, "Đã xảy ra lỗi!", null);
        }
    }
}
