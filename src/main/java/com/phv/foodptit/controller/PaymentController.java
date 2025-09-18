package com.phv.foodptit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @GetMapping("/payment")
    public DataResponse getAllMethodPayment(){
        return this.paymentService.getAllMethodPayment();
    }
}
