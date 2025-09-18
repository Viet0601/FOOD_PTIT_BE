package com.phv.foodptit.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.phv.foodptit.entity.Cart;
import com.phv.foodptit.entity.Food;
import com.phv.foodptit.entity.FoodFavorite;
import com.phv.foodptit.entity.Role;
import com.phv.foodptit.entity.User;
import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.entity.DTO.RegiserResponse;
import com.phv.foodptit.entity.DTO.RegisterRequest;
import com.phv.foodptit.entity.DTO.ResetPasswordRequest;
import com.phv.foodptit.entity.DTO.UserResponse;
import com.phv.foodptit.repository.CartRepository;
import com.phv.foodptit.repository.RoleRepository;
import com.phv.foodptit.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final EmailService emailService;
    private final JwtService jwtService;
    
    public RegiserResponse createNewUser(RegisterRequest request) throws RuntimeException {
        Optional<User>userOptional= this.userRepository.findByEmail(request.getEmail());
        if(userOptional.isPresent()){
            throw new  RuntimeException("Email đã tồn tại!");
        }
        Role role=new Role();
        if(request.getRole()=="" || request.getRole()==null)
        {
            Role userRole= this.roleRepository.findByName("USER").orElseThrow(()->new RuntimeException("Không tìm thấy vai trò người dùng!"));
            role.setId(userRole.getId());
            role.setName(userRole.getName());
        }
        else 
        {
            Role userRole= this.roleRepository.findByName(request.getRole()).orElseThrow(()->new RuntimeException("Không tìm thấy vai trò người dùng!"));
            role.setId(userRole.getId());
            role.setName(userRole.getName());
        }
        User user= new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        User savedUser= this.userRepository.save(user);
        Cart cart = new Cart();
        cart.setCustomer(savedUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        String token= this.jwtService.generateToken(authentication);
        this.cartRepository.save(cart);
        this.emailService.sendWelComeMail(request.getEmail(), request.getFullName());
        RegiserResponse userResponse= new RegiserResponse(request.getEmail(), request.getFullName(), token);
        return userResponse;
        


    }

    public UserResponse getProfile(String email){
        User user = this.userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Không tìm thấy người dùng!"));
        List<Food>list=new ArrayList<>();
        List<FoodFavorite> listfFavorites=user.getListFoodFavorites();
        for(FoodFavorite f:listfFavorites){
            list.add(f.getFood());
        }
        return new UserResponse(user.getEmail(), user.getFullName(), user.getRole(),list);
    }
    public DataResponse sendResetPassword(String email){
        User user= this.userRepository.findByEmail(email)
        .orElseThrow(()->new UsernameNotFoundException("Email chưa được đăng kí!"));
        Random random = new Random();
        Long otp = 100000 + random.nextLong(900000); // từ 100000 -> 999999
        user.setOtp(otp);
        user.setOtpExpired(System.currentTimeMillis() + 5 *60 *1000);
        this.userRepository.save(user);
        this.emailService.sendResetPassword(email, otp);
        return new DataResponse(HttpStatus.SC_OK, "Mã OTP đã được gửi đến email của bạn", null);
    }
    public DataResponse resetPassword(ResetPasswordRequest request)
    {
        User user= this.userRepository.findByEmail(request.getEmail())
        .orElseThrow(()->new UsernameNotFoundException("Email chưa được đăng kí!"));
        if(request.getOtp().length()<6 || !request.getOtp().equals(String.valueOf(user.getOtp())) || user.getOtpExpired()<System.currentTimeMillis() || user.getOtp()==0)
        {
            throw new RuntimeException("OTP không hợp lệ hoặc hết hạn");
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setOtp(0L);
        user.setOtpExpired(0L);
        this.userRepository.save(user);
        return new DataResponse(HttpStatus.SC_OK, "Đổi mật khẩu thành công", null);
        
    }
}
