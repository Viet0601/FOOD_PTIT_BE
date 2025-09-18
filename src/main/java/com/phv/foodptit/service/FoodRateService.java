package com.phv.foodptit.service;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phv.foodptit.entity.Food;
import com.phv.foodptit.entity.FootRate;
import com.phv.foodptit.entity.Order;
import com.phv.foodptit.entity.User;
import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.entity.DTO.RateRequest;
import com.phv.foodptit.repository.FoodRateRepository;
import com.phv.foodptit.repository.FoodRepository;
import com.phv.foodptit.repository.OrderRepository;
import com.phv.foodptit.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodRateService {
    private final FoodRateRepository foodRateRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final OrderRepository orderRepository;

    private String getFoodStar(List<FootRate>list)
    {
        if(list.size()==0)
        {
            return "0";
        }
        double star=0;
        for(FootRate rate:list)
        {
            star+= rate.getStar();
        }
        return String.format("%.1f", star/list.size());
    }

    @Transactional
    public DataResponse createFeedBack(String email,List<RateRequest>listRateRequests,long order_id){
        try {
            User user=this.userRepository.findByEmail(email)
            .orElseThrow(()-> new UsernameNotFoundException("Không tìm thấy người dùng!") );
            List<FootRate> listFoodRate=user.getListFoodRate();
            for(RateRequest request:listRateRequests) {
                FootRate footRate= new FootRate();
                footRate.setCustomer(user);
                Food food = this.foodRepository.findById(request.getFoodId())
                .orElseThrow(()->new RuntimeException(""));
                List<FootRate> listFootRatesForFood= food.getListFoodRates();
                footRate.setFood(food);
                footRate.setFeedback(request.getComment());
                footRate.setStar(request.getRating());
                footRate.setCreatedAt(new Date());
                this.foodRateRepository.save(footRate);
                listFoodRate.add(footRate);
                listFootRatesForFood.add(footRate);
                String star=getFoodStar(listFootRatesForFood);
                food.setStar(star);
                this.foodRepository.save(food);
            }
            this.userRepository.save(user);
            Order order =  this.orderRepository.findById(order_id)
            .orElseThrow(()->new RuntimeException(""));
            order.setFeedback(true);
            this.orderRepository.save(order);
            return new DataResponse(HttpStatus.CREATED.value(), "Đã gửi phản hồi thành công!", null);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return new DataResponse(HttpStatus.BAD_REQUEST.value(), "Đã xảy ra lỗi!", null);
        }
    }


}
