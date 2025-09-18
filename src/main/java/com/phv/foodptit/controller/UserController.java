package com.phv.foodptit.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.entity.DTO.ResetPasswordRequest;
import com.phv.foodptit.entity.DTO.UserResponse;
import com.phv.foodptit.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController

@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/api/v1/profile") 
    public ResponseEntity<DataResponse>getProfile(@CurrentSecurityContext(expression = "authentication?.name")String email){
        UserResponse userResponse= this.userService.getProfile(email);
        DataResponse authResponse=new DataResponse(HttpStatus.OK.value(), null , userResponse);
        return ResponseEntity.ok().body(authResponse);
    }
    @PostMapping("/send-otp")
    public DataResponse sendOTP(@RequestBody Map<String, String> body)
    {
        String email = body.get("email");
        return this.userService.sendResetPassword(email);
    }
    @PutMapping("reset-password")
     public DataResponse resetPassword(@RequestBody ResetPasswordRequest request)
    {
        return this.userService.resetPassword(request);
    }
}
