package com.phv.foodptit.controller;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.phv.foodptit.entity.DTO.AuthResponse;
import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.entity.DTO.LoginRequest;
import com.phv.foodptit.entity.DTO.RegiserResponse;
import com.phv.foodptit.entity.DTO.RegisterRequest;

import com.phv.foodptit.service.CustomUsersDetailsService;
import com.phv.foodptit.service.JwtService;
import com.phv.foodptit.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
// @RequestMapping(JavaConstant.API_PREFIX)
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final CustomUsersDetailsService customUsersDetailsService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @PostMapping("/register")
    public ResponseEntity<DataResponse> createNewUser( @Valid @RequestBody RegisterRequest request){
        DataResponse authResponse= new DataResponse();
        RegiserResponse  userResponse = this.userService.createNewUser(request);
        authResponse.setEc(HttpStatus.CREATED.value());
        authResponse.setEm("Đăng kí tài khoản thành công!");
        authResponse.setDt(userResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);

    }
    @PostMapping("/login")
    public ResponseEntity<DataResponse>loginUser(@Valid @RequestBody LoginRequest request){
         DataResponse dataResponse=new DataResponse();
        try {
            // UserDetails  userDetails =this.customUsersDetailsService.loadUserByUsername(request.getEmail());
            
            Authentication authentication= authenticated(request.getEmail(),request.getPassword());
            String accessToken= this.jwtService.generateToken(authentication);
            String refreshToken= this.jwtService.generateRefeshToken(authentication);
            // String token= this.jwtService.generateToken(authentication);
            ResponseCookie cookie= ResponseCookie.from("refresh_token",refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(Duration.ofDays(7))
            .sameSite("None")
            .build();
            AuthResponse authResponse= new AuthResponse(accessToken, refreshToken);
            dataResponse.setEc(HttpStatus.OK.value());
            dataResponse.setEm("Đăng nhập thành công!");
            dataResponse.setDt(authResponse);
             return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString()).body(dataResponse);
        } catch (Exception e) {
            // TODO: handle exception
            dataResponse.setEc(HttpStatus.BAD_REQUEST.value());
            dataResponse.setEm("Sai tài khoản hoặc mật khẩu!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dataResponse);
        }
    }
     @PostMapping("/logout")
    public ResponseEntity<DataResponse>Logout(HttpServletResponse response){
        ResponseCookie cookie= ResponseCookie.from("refresh_token","")
        .httpOnly(true)
        .path("/")
        .maxAge(0)
        .sameSite("None").build();
        DataResponse dataResponse= new DataResponse();
        dataResponse.setEc(HttpStatus.OK.value());
      
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(dataResponse);
    }
    private Authentication  authenticated(String email,String password){
         return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
       // Lấy refresh token từ cookie
    String refreshToken = null;
    if (request.getCookies() != null) {
        for (Cookie cookie : request.getCookies()) {
            if ("refresh_token".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
            }
        }
    }

    if (refreshToken == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Refresh token is missing"));
    }

    try {
        // Kiểm tra hợp lệ refresh token
        String email = jwtService.getEmail(refreshToken); 
        UserDetails userDetails = this.customUsersDetailsService.loadUserByUsername(email);
        if (jwtService.isValidateToken(refreshToken, userDetails)) {
            // Tạo access token mới
            Authentication authentication = 
                new UsernamePasswordAuthenticationToken(email, null, 
                    customUsersDetailsService.loadUserByUsername(email).getAuthorities());

            String newAccessToken = jwtService.generateToken(authentication);

            // Có thể set lại refresh token (nếu muốn xoay vòng)
            ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .sameSite("None")
                    .maxAge(7 * 24 * 60 * 60) // 7 ngày
                    .build();
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(Map.of("dt", newAccessToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid refresh token"));
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid or expired refresh token"));
    }
    }
}
