package com.phv.foodptit.entity.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Vui lòng nhập email!")
    private String email;
    @NotBlank(message = "Vui lòng nhập mật khẩu!")
    private String password;
}
