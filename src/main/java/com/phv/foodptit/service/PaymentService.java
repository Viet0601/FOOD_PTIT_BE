package com.phv.foodptit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.phv.foodptit.entity.Order;
import com.phv.foodptit.entity.OrderItem;
import com.phv.foodptit.entity.Payment;
import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.entity.DTO.PaymentResponse;
import com.phv.foodptit.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    @Value("${spring.frontend-url}")
    private String FE_URL;
     @Value("${stripe.api.key}")
    private String STRIPE_API_KEY;
    private final PaymentRepository paymentRepository;
    public DataResponse getAllMethodPayment(){
        try {
           List<Payment> list= this.paymentRepository.findAll();
           return new DataResponse(HttpStatus.OK.value(), null, list);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return new DataResponse(HttpStatus.BAD_REQUEST.value(), null, null);
        }
    }
       public PaymentResponse createPaymentLink(Order order) throws Exception{
        Stripe.apiKey=STRIPE_API_KEY;
        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl(FE_URL+"/payment/success?orderId=" + order.getId())
    .setCancelUrl(FE_URL+"/payment/fail?orderId=" + order.getId());

// Lặp qua từng món ăn trong đơn hàng và add vào line items
for (OrderItem item : order.getListOrderItems()) {
    paramsBuilder.addLineItem(
        SessionCreateParams.LineItem.builder()
            .setQuantity((long) item.getQuantity())
            .setPriceData(
                SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("vnd")
                    .setUnitAmount(item.getFood().getPrice()) // USD → cents
                    .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(item.getFood().getName()) // Tên món ăn
                            .build()
                    )
                    .build()
            )
            .build()
    );
}

SessionCreateParams params = paramsBuilder.build();
Session session = Session.create(params);

return new PaymentResponse(session.getUrl());
       
    }
}
