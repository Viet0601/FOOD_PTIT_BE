package com.phv.foodptit.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    private String phone;
    private String village;
    private String district;
    private String province;
    private String detail;
}
