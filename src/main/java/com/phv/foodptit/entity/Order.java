package com.phv.foodptit.entity;

import java.util.Date;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`orders`")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private boolean isPaid;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User customer;

   
    @ManyToOne
    @JoinColumn(name = "status_order_id")
    private StatusOrder statusOrder;
   
    @JsonIgnore
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderItem>listOrderItems;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;
    private Date createdAt;
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
    private long total;
    private boolean isFeedback;
}
