package com.phv.foodptit.entity.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    
    @NotBlank(message = "Vui lòng điền họ và tên!")
    private String fullName;
    @Email(message = "Email không hợp lệ!")
    @NotBlank(message ="Vui lòng nhập email hợp lệ!" )
    @Column(unique = true)
    private String email;
    @Size(min = 6,message = "Mật khẩu ít nhất 6 kí tự")
    private String password;
    private String role;
}
