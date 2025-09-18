package com.phv.foodptit.entity.DTO;

import java.util.Date;
import java.util.List;

import com.phv.foodptit.entity.Address;
import com.phv.foodptit.entity.StatusOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private long id;
    private List<OrderItemResponse>listOrderItems;
    private long total;
    private StatusOrder statusOrder;
    private boolean feedback;
    private Date createdAt;
    private Address address;
    private boolean isPaid;
}
