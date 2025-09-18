package com.phv.foodptit.entity.DTO;

import java.util.List;

import com.phv.foodptit.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderAdminResponse {
    private long totalPage;
    private long currentPage;
    List<OrderResponse> listOrders;
}
