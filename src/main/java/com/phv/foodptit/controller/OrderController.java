package com.phv.foodptit.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phv.foodptit.entity.Order;
import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.entity.DTO.OrderRequest;
import com.phv.foodptit.entity.DTO.PaymentResponse;
import com.phv.foodptit.entity.DTO.UpdateStatusRequest;
import com.phv.foodptit.service.OrderService;
import com.phv.foodptit.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    private final PaymentService paymentService;
    @PostMapping("/order")
    public DataResponse createOrder(@CurrentSecurityContext(expression="authentication?.name") String email,@RequestBody OrderRequest request){
        Order order= this.orderService.createOrder(email, request);
        if(order !=null)
        {
           
            try {
               PaymentResponse paymentResponse= this.paymentService.createPaymentLink(order);
                 return new DataResponse(HttpStatus.CREATED.value(), null, paymentResponse);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                 return new DataResponse(HttpStatus.BAD_REQUEST.value(), null, null);
            }
      
        }
        else 
        {
            return new DataResponse(HttpStatus.BAD_REQUEST.value(), null, null);
        }
       
    }
    @GetMapping("/order")
    public DataResponse getAllOrder(@CurrentSecurityContext(expression="authentication?.name") String email,
    @RequestParam String page,@RequestParam String limit
    ){
        Pageable pageable= PageRequest.of(Integer.parseInt(page)-1,Integer.parseInt(limit),Sort.by(Sort.Direction.DESC, "createdAt"));
        return this.orderService.getAllOrder(email,pageable);
    }
    @DeleteMapping("/order/cancel/{id}")
    public DataResponse cancelOrder(@CurrentSecurityContext(expression="authentication?.name") String email,@PathVariable String id){
        return this.orderService.cancelOrder(email, Long.parseLong(id));
    }
    @PutMapping("/admin/order/status")
    public DataResponse updateStatusOrder(@CurrentSecurityContext(expression="authentication?.name") String email,@RequestBody UpdateStatusRequest request){
        return this.orderService.updateStatusOrder(email, request);
    }
    @GetMapping("admin/order")
    public DataResponse GetAllOrderAdmin(@RequestParam String page,@RequestParam String limit){
    Pageable pageable= PageRequest.of(Integer.parseInt(page)-1, Integer.parseInt(limit),Sort.by(Sort.Direction.DESC, "createdAt"));
        return this.orderService.getAllOrderAdmin(pageable);
    }
    @PutMapping("/order/payment-success/{id}")
    public void updateSuccessPayment(@PathVariable String id)
    {
        this.orderService.updateOrderPaidSuccess(Long.parseLong(id));
    }

}
