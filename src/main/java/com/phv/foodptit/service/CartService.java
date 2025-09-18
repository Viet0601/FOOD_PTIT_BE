package com.phv.foodptit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phv.foodptit.entity.Cart;
import com.phv.foodptit.entity.CartItem;
import com.phv.foodptit.entity.Food;
import com.phv.foodptit.entity.User;
import com.phv.foodptit.entity.DTO.CartItemRequest;
import com.phv.foodptit.entity.DTO.CartItemResponse;
import com.phv.foodptit.entity.DTO.CartResponse;
import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.repository.CartItemRepository;
import com.phv.foodptit.repository.CartRepository;
import com.phv.foodptit.repository.FoodRepository;
import com.phv.foodptit.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final FoodRepository foodRepository;
    private final CartItemRepository cartItemRepository;
    @Transactional
    public DataResponse addItemToCart(String email,CartItemRequest request){
        try {
            User user=this.userRepository.findByEmail(email)
            .orElseThrow(()->new UsernameNotFoundException("Không tìm thấy người dùng!"));
            Cart cart=this.cartRepository.findByCustomer(user)
            .orElseThrow(()->new RuntimeException(""));
            Food food=this.foodRepository.findById(request.getFoodId())
            .orElseThrow(()->new RuntimeException(""));
             Optional<CartItem> cartItemOptional= this.cartItemRepository.findByCartAndFood(cart, food);
            if(cartItemOptional.isPresent())
            {
                CartItem cartItem = cartItemOptional.get();
                cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
                cartItem.setTotalPrice(cartItem.getTotalPrice() + request.getQuantity() * food.getPrice());
                this.cartItemRepository.save(cartItem);

            }
            else 
            {
                 long total=food.getPrice() * request.getQuantity();
             CartItem cartItem=new CartItem();
            cartItem.setCart(cart);
            cartItem.setFood(food);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setTotalPrice(total);
            this.cartItemRepository.save(cartItem);
             List<CartItem> listCartItemsUser= cart.getListCartItem();
            listCartItemsUser.add(cartItem);
            
            }

           
           
            
            List<CartItem> listCartItems=this.cartItemRepository.findByCart(cart);

            long totalCart=0;
            for(CartItem item:listCartItems){
                totalCart+= item.getTotalPrice();
            }
            cart.setTotal(totalCart);
           
            this.cartRepository.save(cart);
             List<CartItem> listCart=cart.getListCartItem();
            List<CartItemResponse> list =new  ArrayList<>();
            for(CartItem item:listCart){
                CartItemResponse cartItemResponse=new CartItemResponse();
                cartItemResponse.setId(item.getId());
                cartItemResponse.setFood(item.getFood());
                cartItemResponse.setQuantity(item.getQuantity());
                cartItemResponse.setTotalPrice(item.getTotalPrice());
                list.add(cartItemResponse);
            }
            CartResponse cartResponse=new CartResponse(list, cart.getTotal());
            return new DataResponse(HttpStatus.SC_OK,"Thêm vào giỏ hàng thành công!",cartResponse);

        } catch (Exception e) {
            // TODO: handle exception
            return new DataResponse(HttpStatus.SC_BAD_REQUEST, null, null);
        }
    }
    // @Transactional
    // public DataResponse updateItemQuantity(String email,CartItemRequest request){
    //     try {
    //         User user=this.userRepository.findByEmail(email)
    //         .orElseThrow(()->new UsernameNotFoundException("Không tìm thấy người dùng!"));
    //         Cart cart=this.cartRepository.findByCustomer(user)
    //         .orElseThrow(()->new RuntimeException(""));
    //         Food food=this.foodRepository.findById(request.getFoodId())
    //         .orElseThrow(()->new RuntimeException(""));
    //         Optional<CartItem> cartItemOptional= this.cartItemRepository.findByCartAndFood(cart, food);
            
    //         if(cartItemOptional.isPresent())
    //         {
    //             CartItem cartItem=cartItemOptional.get();
    //             cartItem.setQuantity(cartItem.getQuantity()+ request.getQuantity());
    //             this.cartItemRepository.save(cartItem);
    //         }
           
    //          cartItem.setQuantity(request.getQuantity());
    //          cartItem.setTotalPrice(request.getQuantity() * food.getPrice());
    //         this.cartItemRepository.save(cartItem);
    //         List<CartItem> listCartItems= cart.getListCartItem();
    //         long totalCartNew=0;
    //         for(CartItem item:listCartItems){
    //             if (item.getFood().getId() == request.getFoodId()){
    //                 item.setQuantity(request.getQuantity());
    //                 item.setTotalPrice(item.getFood().getPrice() * request.getQuantity());
    //                 totalCartNew+= (item.getFood().getPrice() * request.getQuantity());
    //             }
    //             else 
    //             {
    //                 totalCartNew+= item.getTotalPrice();
    //             }
    //         }
    //         cart.setTotal(totalCartNew);
    //         this.cartRepository.save(cart);
            
    //          return new DataResponse(HttpStatus.SC_OK, null, null);
            
    //     } catch (Exception e) {
    //         // TODO: handle exception
    //          e.printStackTrace();
    //          return new DataResponse(HttpStatus.SC_BAD_REQUEST, null, null);
    //     }
        
    // }
    @Transactional
    public DataResponse removeItemFromCart(String email,long cartItemId){
        try {
            User user=this.userRepository.findByEmail(email)
            .orElseThrow(()->new UsernameNotFoundException("Không tìm thấy người dùng!"));
            Cart cart=this.cartRepository.findByCustomer(user)
            .orElseThrow(()->new RuntimeException(""));
            CartItem cartItem=this.cartItemRepository.findById(cartItemId)
            .orElseThrow(()->new RuntimeException(""));
            cart.getListCartItem().remove(cartItem);
            List<CartItem> list= cart.getListCartItem();
            cart.setTotal(caculateCartTotal(list));
            this.cartRepository.save(cart);
            return new DataResponse(HttpStatus.SC_OK, "Đã xóa món ăn khỏi giỏ hàng!", null);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return new DataResponse(HttpStatus.SC_BAD_REQUEST, "Đã có lỗi xảy ra!", null);
        }
    }
    private long caculateCartTotal(  List<CartItem> list){
        long total= 0;
        for(CartItem item : list){
            total+= (item.getFood().getPrice() * item.getQuantity());
        }
        return total;
       
    }
    @Transactional
    public DataResponse clearCartItem(String email){
        try {
             User user=this.userRepository.findByEmail(email)
            .orElseThrow(()->new UsernameNotFoundException("Không tìm thấy người dùng!"));
            Cart cart=this.cartRepository.findByCustomer(user)
             .orElseThrow(()->new RuntimeException(""));
             cart.getListCartItem().clear();
             cart.setTotal(0L);
             
             this.cartRepository.save(cart);
             return new DataResponse(HttpStatus.SC_OK, null, null);
        } catch (Exception e) {
            // TODO: handle exception
            return new DataResponse(HttpStatus.SC_BAD_REQUEST, null, null);
        }
    }
    public DataResponse getCart(String email){
        try {
             User user=this.userRepository.findByEmail(email)
            .orElseThrow(()->new UsernameNotFoundException("Không tìm thấy người dùng!"));
            Cart cart=this.cartRepository.findByCustomer(user)
            .orElseThrow(()->new RuntimeException(""));
            List<CartItem> listCartItems=cart.getListCartItem();
            List<CartItemResponse> list =new  ArrayList<>();
            for(CartItem item:listCartItems){
                CartItemResponse cartItemResponse=new CartItemResponse();
                cartItemResponse.setId(item.getId());
                cartItemResponse.setFood(item.getFood());
                cartItemResponse.setQuantity(item.getQuantity());
                cartItemResponse.setTotalPrice(item.getTotalPrice());
                list.add(cartItemResponse);
            }
            CartResponse cartResponse=new CartResponse(list, cart.getTotal());
            return new DataResponse(HttpStatus.SC_OK,null,cartResponse);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return new DataResponse(HttpStatus.SC_BAD_REQUEST, null, null);
        }
    }
}
