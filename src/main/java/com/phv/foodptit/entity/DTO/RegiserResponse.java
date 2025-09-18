package com.phv.foodptit.entity.DTO;

import com.phv.foodptit.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegiserResponse {
    private String email;
    private String fullName;
    private String token;
}
