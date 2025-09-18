package com.phv.foodptit.controller;

import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phv.foodptit.entity.DTO.AddressRequest;
import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.service.AddressService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    @PostMapping("/address")
    public DataResponse createNewAddress(@CurrentSecurityContext(expression = "authentication?.name") String email,@RequestBody AddressRequest request){
        return this.addressService.createNewAddress(email, request);
    }
    @PutMapping("/address/{id}")
    public DataResponse updateAddress(@CurrentSecurityContext(expression = "authentication?.name") String email,@PathVariable String id,@RequestBody AddressRequest request){
        return this.addressService.UpdateAddress(Long.parseLong(id), email, request);
    }
    @DeleteMapping("/address/{id}")
    public DataResponse deleteAddress(@CurrentSecurityContext(expression = "authentication?.name") String email,@PathVariable String id){
        return this.addressService.DeteleAddress(Long.parseLong(id), email);
    }
    @GetMapping("/address")
    public DataResponse getAllAddress(@CurrentSecurityContext(expression = "authentication?.name") String email){
        return this.addressService.getAllAddress(email);
    }


}
