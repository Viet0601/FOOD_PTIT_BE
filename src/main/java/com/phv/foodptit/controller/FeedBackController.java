package com.phv.foodptit.controller;

import java.util.List;

import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.entity.DTO.FeedBackRequest;
import com.phv.foodptit.entity.DTO.RateRequest;
import com.phv.foodptit.service.FoodRateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FeedBackController {
    private final FoodRateService foodRateService;
    @PostMapping("/feedback/{orderId}")
    public DataResponse createNewFeedBack(@CurrentSecurityContext(expression = "authentication?.name")String email,@RequestBody List<RateRequest> listRate,@PathVariable String orderId){
        return this.foodRateService.createFeedBack(email, listRate,Long.parseLong(orderId));
    }
}
