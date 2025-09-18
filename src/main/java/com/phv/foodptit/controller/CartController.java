package com.phv.foodptit.controller;

import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phv.foodptit.entity.DTO.CartItemRequest;
import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/cart/add")
    public DataResponse addToCartItem(@CurrentSecurityContext(expression = "authentication?.name") String email,@RequestBody CartItemRequest request){
        return this.cartService.addItemToCart(email, request);
    }
    @GetMapping("/cart")
    public DataResponse getMyCart(@CurrentSecurityContext(expression = "authentication?.name") String email){
        return this.cartService.getCart(email);
    }
    // @PutMapping("/cart/update")
    //  public DataResponse updateQuanityItemCart(@CurrentSecurityContext(expression = "authentication?.name") String email,@RequestBody CartItemRequest request){
    //     return this.cartService.updateItemQuantity(email, request);
    // }
    @DeleteMapping("/cart/delete/{id}")
    public DataResponse deleteItemCart(@CurrentSecurityContext(expression = "authentication?.name") String email,@PathVariable String id){
        return this.cartService.removeItemFromCart(email, Long.parseLong(id));
    }
    @PutMapping("/cart/clear")
    public DataResponse clearCartItem(@CurrentSecurityContext(expression = "authentication?.name") String email){
        return this.cartService.clearCartItem(email);
    }
}
