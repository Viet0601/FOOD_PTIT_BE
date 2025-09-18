package com.phv.foodptit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.service.StatusOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class StatusOrderController {
    private final StatusOrderService statusOrderService;

    @GetMapping("/status-order")
    public DataResponse GetAllStatusOrder(){
        return this.statusOrderService.getAllStatusOrder();
    }
}
