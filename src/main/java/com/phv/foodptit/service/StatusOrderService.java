package com.phv.foodptit.service;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.phv.foodptit.entity.StatusOrder;
import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.repository.StatusOrderRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class StatusOrderService {
   
    private final StatusOrderRepository statusOrderRepository;

    public DataResponse getAllStatusOrder(){
        List<StatusOrder> list =this.statusOrderRepository.findAll();
        return new DataResponse(HttpStatus.SC_OK, null, list);
    }
 
}
