package com.phv.foodptit.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phv.foodptit.entity.Category;
import com.phv.foodptit.entity.Food;
import com.phv.foodptit.entity.FoodFavorite;
import com.phv.foodptit.entity.Food_Ingredient;
import com.phv.foodptit.entity.Food_Nutrition;
import com.phv.foodptit.entity.FootRate;
import com.phv.foodptit.entity.Ingredient;
import com.phv.foodptit.entity.Nutrition;
import com.phv.foodptit.entity.User;
import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.entity.DTO.FeedbackResponse;
import com.phv.foodptit.entity.DTO.FoodFeedbackResponse;
import com.phv.foodptit.entity.DTO.FoodPaginateResponse;
import com.phv.foodptit.entity.DTO.FoodRequest;
import com.phv.foodptit.entity.DTO.FoodResponse;
import com.phv.foodptit.entity.DTO.IngredientRequest;
import com.phv.foodptit.entity.DTO.IngredientResponse;
import com.phv.foodptit.entity.DTO.NutritionRequest;
import com.phv.foodptit.entity.DTO.NutritionResponse;
import com.phv.foodptit.repository.CategoryRepository;
import com.phv.foodptit.repository.FoodFavoritRepository;
import com.phv.foodptit.repository.FoodRepository;
import com.phv.foodptit.repository.Food_IngredientRepository;
import com.phv.foodptit.repository.Food_NutritrionRepository;
import com.phv.foodptit.repository.IngredientRepository;
import com.phv.foodptit.repository.NutritionRepository;
import com.phv.foodptit.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final CategoryRepository categoryRepository;
    private final FoodRepository foodRepository;
    private final NutritionRepository nutritionRepository;
    private final Food_NutritrionRepository food_NutritrionRepository;
    private final Food_IngredientRepository food_IngredientRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;
    private final FoodFavoritRepository foodFavoritRepository;
    @Transactional
    public DataResponse createNewFood(List<String>images,FoodRequest foodRequest,List<IngredientRequest>listIngredient,List<NutritionRequest>listNutritions){
       
        DataResponse dataResponse=new DataResponse();
        try {
        Category category = this.categoryRepository.findById(foodRequest.getCategory_id())
        .orElseThrow(()-> new RuntimeException("Không tìm thấy danh mục này!"));
        Food food=new Food();
        food.setName(foodRequest.getName());
        food.setDescription(foodRequest.getDescription());
        food.setPrice(foodRequest.getPrice());
        food.setImages(images);
        food.setCategory(category);
        food.setAvailable(foodRequest.isAvailable());
        food.setVegetarian(foodRequest.isVegetarian());
        this.foodRepository.save(food);
        
        List<Food_Ingredient>listFood_Ingredients=food.getListIngredients();
        for(IngredientRequest rq:listIngredient){
            long ingredient_id=rq.getId();
            Ingredient ingredient=this.ingredientRepository.findById(ingredient_id)
            .orElseThrow(
                ()->new RuntimeException("Không tìm thấy nguyên liệu!"));
            Food_Ingredient food_Ingredient=new Food_Ingredient();
            food_Ingredient.setFood(food);
            food_Ingredient.setIngredient(ingredient);
            food_Ingredient.setQuantity(rq.getAmount());
            this.food_IngredientRepository.save(food_Ingredient);
            listFood_Ingredients.add(food_Ingredient);
        }
        List<Food_Nutrition>lisfFood_Nutritions=food.getListNutritions();
        for(NutritionRequest rq:listNutritions){
            long nutrition_id=rq.getId();
            Nutrition nutrition=this.nutritionRepository.findById(nutrition_id)
            .orElseThrow(
                ()->new RuntimeException("Không tìm thấy dinh dưỡng này!"));
            Food_Nutrition food_Nutrition=new Food_Nutrition();
            food_Nutrition.setFood(food);
            food_Nutrition.setNutrition(nutrition);
            food_Nutrition.setAmount(rq.getAmount());
            this.food_NutritrionRepository.save(food_Nutrition);
            lisfFood_Nutritions.add(food_Nutrition);
        }
        food.setListNutritions(lisfFood_Nutritions);
        food.setStar("0");
        food.setServing(foodRequest.getServing());
        food.setShipTime(foodRequest.getShipTime());
        food.setCreatedAt(new Date());
        this.foodRepository.save(food);
        dataResponse.setEc(HttpStatus.SC_CREATED);
        dataResponse.setEm("Thêm mới món ăn thành công!");
        dataResponse.setDt(food);


        } catch (Exception e) {
            // TODO: handle exception
               dataResponse.setEc(HttpStatus.SC_BAD_REQUEST);
            dataResponse.setEm("Thêm mới món ăn thất bại!");
        }
        return dataResponse;
    }
    @Transactional
    public DataResponse updateFood(List<String>images,FoodRequest foodRequest,long id,List<IngredientRequest>listIngredient,List<NutritionRequest>listNutritions){
       
        DataResponse dataResponse=new DataResponse();
        try {
        Category category = this.categoryRepository.findById(foodRequest.getCategory_id())
        .orElseThrow(()-> new RuntimeException("Không tìm thấy danh mục này!"));
        Food food=this.foodRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Không tìm thấy món ăn này!"));
        food.setName(foodRequest.getName());
        food.setDescription(foodRequest.getDescription());
        food.setPrice(foodRequest.getPrice());
        food.setImages(images);
        food.setCategory(category);
        food.setAvailable(foodRequest.isAvailable());
        food.setVegetarian(foodRequest.isVegetarian());
        // update ingredient
        List<Food_Ingredient>listFood_Ingredients=food.getListIngredients();
        for(IngredientRequest rq:listIngredient){
             Ingredient ingredient=this.ingredientRepository.findById(rq.getId())
            .orElseThrow(
                ()->new RuntimeException("Không tìm thấy nguyên liệu!")); 
            Food_Ingredient food_Ingredient=this.food_IngredientRepository.findById(rq.getUpdateId())
            .orElseThrow(
                ()->new RuntimeException("Không tìm thấy nguyên liệu!")); 
            food_Ingredient.setFood(food);
            food_Ingredient.setIngredient(ingredient);
            food_Ingredient.setQuantity(rq.getAmount());
            this.food_IngredientRepository.save(food_Ingredient);
            listFood_Ingredients.add(food_Ingredient);
        }
        // update nutrition
        List<Food_Nutrition>listFood_Nutritions=food.getListNutritions();
        for(NutritionRequest rq:listNutritions){
             Nutrition nutrition=this.nutritionRepository.findById(rq.getId())
            .orElseThrow(
                ()->new RuntimeException("Không tìm thấy dinh dưỡng này!")); 
            Food_Nutrition food_Nutrition=this.food_NutritrionRepository.findById(rq.getUpdateId())
            .orElseThrow(
                ()->new RuntimeException("Không tìm thấy nguyên liệu!"));
            food_Nutrition.setFood(food);
            food_Nutrition.setNutrition(nutrition);
            food_Nutrition.setAmount(rq.getAmount());
            this.food_NutritrionRepository.save(food_Nutrition);
            listFood_Nutritions.add(food_Nutrition);
        }


        food.setListIngredients(listFood_Ingredients);
        food.setListNutritions(listFood_Nutritions);
        food.setServing(foodRequest.getServing());
        food.setShipTime(foodRequest.getShipTime());
        this.foodRepository.save(food);
        dataResponse.setEc(HttpStatus.SC_OK);
        dataResponse.setEm("Cập nhật món ăn thành công!");
        dataResponse.setDt(food);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
               dataResponse.setEc(HttpStatus.SC_BAD_REQUEST);
            dataResponse.setEm("Cập nhật món ăn thất bại!");
        }
        return dataResponse;
    }
    @Transactional
    public DataResponse deteleFood(long id){
        DataResponse response= new DataResponse();
        try {
            this.foodRepository.deleteById(id);
            response.setEc(HttpStatus.SC_OK);
            response.setEm("Xóa món ăn thành công!");
        } catch (Exception e) {
            // TODO: handle exception
             response.setEc(HttpStatus.SC_BAD_REQUEST);
            response.setEm("Có lỗi xảy ra!");
        }
        return response;
    }
    
    public DataResponse getALlFood(Pageable pageable,String category){
        List<Food>list=new ArrayList<>();
        if(pageable!=null){
            if(category.equals("ALL"))
            {
        Page<Food>listPage= this.foodRepository.findAll(pageable);
       for(Food food:listPage){
       
        list.add(food);
        
       }
       FoodPaginateResponse foodPaginateResponse=new FoodPaginateResponse();
       foodPaginateResponse.setListFood(list);
       foodPaginateResponse.setCurrentPage(listPage.getNumber() + 1);
       foodPaginateResponse.setTotalPage(listPage.getTotalPages());
       DataResponse dataResponse = new DataResponse();
       dataResponse.setEc(HttpStatus.SC_OK);
       dataResponse.setDt(foodPaginateResponse);
       return dataResponse;
            }
            else 
            {
                long category_id= Long.parseLong(category);
                Category categoryItem= this.categoryRepository.findById(category_id)
                .orElseThrow(()->new RuntimeException("Không tìm thấy danh mục này!"));
                Page<Food> listPage= this.foodRepository.findByCategory(categoryItem, pageable);
                for(Food food:listPage){
       
                      list.add(food);
        
             }
             FoodPaginateResponse foodPaginateResponse=new FoodPaginateResponse();
       foodPaginateResponse.setListFood(list);
       foodPaginateResponse.setCurrentPage(listPage.getNumber() + 1);
       foodPaginateResponse.setTotalPage(listPage.getTotalPages());
       DataResponse dataResponse = new DataResponse();
       dataResponse.setEc(HttpStatus.SC_OK);
       dataResponse.setDt(foodPaginateResponse);
       return dataResponse;
            }
         }
       else 
       {
        list=this.foodRepository.findAll();
         DataResponse dataResponse = new DataResponse();
       dataResponse.setEc(HttpStatus.SC_OK);
       dataResponse.setDt(list);
       return dataResponse;
       }

      
    }
    public DataResponse SearchFood(String query){
        List<Food> list=this.foodRepository.findBySearchQuery(query);
        return new DataResponse(HttpStatus.SC_OK, "", list);
    }
    @Transactional
    public DataResponse addFoodFavorite(String email,long foodId){
        DataResponse dataResponse =new DataResponse();
        try {
            User user=this.userRepository.findByEmail(email)
        .orElseThrow(()->new UsernameNotFoundException("Không tìm thấy người dùng!"));
         Food food=this.foodRepository.findById(foodId)
        .orElseThrow(()-> new RuntimeException("Không tìm thấy món ăn này!"));
        Optional<FoodFavorite>foodFavorite= this.foodFavoritRepository.findByCustomerAndFood(user, food);
        List<Food>listFoods=new ArrayList<>();
        if(foodFavorite.isPresent()){

            user.getListFoodFavorites().remove(foodFavorite.get());
            this.userRepository.save(user);
            List<FoodFavorite> list =user.getListFoodFavorites();
            for(FoodFavorite f: list)
            {
                listFoods.add(f.getFood());
            }
            dataResponse.setEm("Đã gỡ khỏi mục yêu thích!");
        }
        else 
        {
            List<FoodFavorite>list=user.getListFoodFavorites();
            FoodFavorite newFood=new FoodFavorite();
            newFood.setCustomer(user);
            newFood.setFood(food);
            this.foodFavoritRepository.save(newFood);
            list.add(newFood);
            user.setListFoodFavorites(list);
            this.userRepository.save(user);
            for(FoodFavorite f:list){
                listFoods.add(f.getFood());
            }
             dataResponse.setEm("Đã thêm vào danh mục yêu thích!");
        }
        dataResponse.setEc(HttpStatus.SC_OK);
       
        dataResponse.setDt(listFoods);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            dataResponse.setEc(HttpStatus.SC_BAD_REQUEST);
            dataResponse.setEm("Đã xảy ra lỗi!");
        }
        return dataResponse;
        
    }
    public DataResponse getFoodByCategory(long category_id){
        DataResponse dataResponse=new DataResponse();
        try {
            Category category=this.categoryRepository.findById(category_id)
            .orElseThrow(()->new RuntimeException("Không tìm thấy danh mục này"));
            List<Food> list= this.foodRepository.findByCategory(category);
            return new DataResponse(HttpStatus.SC_OK, null, list);
            
        } catch (Exception e) {
            // TODO: handle exception
            return new DataResponse(HttpStatus.SC_BAD_REQUEST, "Đã xảy ra lỗi!", null);
        }
    }
    public DataResponse getFoodById(long id){
        try {
        Food food=this.foodRepository.findById(id)  .orElseThrow(()-> new RuntimeException("Không tìm thấy món ăn này!"));
        List<IngredientResponse> list=new ArrayList<>();
        for(Food_Ingredient fi:food.getListIngredients()){
            IngredientResponse ingredientResponse=new IngredientResponse();
            ingredientResponse.setIngredientId(fi.getIngredient().getId());
            ingredientResponse.setName(fi.getIngredient().getName());
            ingredientResponse.setAmount(fi.getQuantity());
            ingredientResponse.setId(fi.getId());
            ingredientResponse.setUnit(fi.getIngredient().getUnit());
            list.add(ingredientResponse);
        }
        List<NutritionResponse> listNutrition=new ArrayList<>();
        for(Food_Nutrition fi:food.getListNutritions()){
            NutritionResponse nutritionResponse=new NutritionResponse();
            nutritionResponse.setId(fi.getId());
            nutritionResponse.setName(fi.getNutrition().getName());
            nutritionResponse.setAmount(fi.getAmount());
            nutritionResponse.setNutritionId(fi.getNutrition().getId());
            listNutrition.add(nutritionResponse);
        }
        List<FootRate> listFoodRates=food.getListFoodRates();
        List<FoodFeedbackResponse> listFeedbackResponses=new ArrayList<>();
        for(FootRate rate:listFoodRates)
        {
            FoodFeedbackResponse foodFeedbackResponse= new FoodFeedbackResponse();
            foodFeedbackResponse.setName(rate.getCustomer().getFullName());
            foodFeedbackResponse.setCreatedAt(rate.getCreatedAt());
            foodFeedbackResponse.setFeedback(rate.getFeedback());
            foodFeedbackResponse.setStar(rate.getStar());
            listFeedbackResponses.add(foodFeedbackResponse);
        }
        FeedbackResponse feedbackResponse = new FeedbackResponse(listFeedbackResponses, food.getListFoodRates().size());
         FoodResponse foodResponse =new FoodResponse(feedbackResponse,food.getCategory(),food.getImages(),list,listNutrition, food.getId(), food.getName(), food.getDescription(), food.getPrice(), food.isAvailable(), food.isVegetarian(),food.getServing(),food.getShipTime(),food.getStar(),food.getListFoodRates().size());
        return new DataResponse(HttpStatus.SC_OK, null, foodResponse);
      
       
        } catch (Exception e) {
            // TODO: handle exception
             return new DataResponse(HttpStatus.SC_BAD_REQUEST, null, null);
        }
    }
}
