package com.phv.foodptit.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phv.foodptit.entity.Address;
import com.phv.foodptit.entity.Cart;
import com.phv.foodptit.entity.CartItem;
import com.phv.foodptit.entity.Order;
import com.phv.foodptit.entity.OrderItem;
import com.phv.foodptit.entity.Payment;
import com.phv.foodptit.entity.StatusOrder;
import com.phv.foodptit.entity.User;
import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.entity.DTO.OrderAdminResponse;
import com.phv.foodptit.entity.DTO.OrderItemResponse;
import com.phv.foodptit.entity.DTO.OrderPaginateResponse;
import com.phv.foodptit.entity.DTO.OrderRequest;
import com.phv.foodptit.entity.DTO.OrderResponse;
import com.phv.foodptit.entity.DTO.UpdateStatusRequest;
import com.phv.foodptit.repository.AddressRepository;
import com.phv.foodptit.repository.CartRepository;
import com.phv.foodptit.repository.OrderRepository;
import com.phv.foodptit.repository.PaymentRepository;
import com.phv.foodptit.repository.StatusOrderRepository;
import com.phv.foodptit.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final StatusOrderRepository statusOrderRepository;
    private final AddressRepository addressRepository;
    private final PaymentRepository paymentRepository;
    @Transactional
    public Order createOrder(String email,OrderRequest request){
        try {
             User user=this.userRepository.findByEmail(email)
            .orElseThrow(()->new UsernameNotFoundException("Không tìm thấy người dùng!"));
            Cart cart=this.cartRepository.findByCustomer(user)
            .orElseThrow(()->new RuntimeException(""));
            Address address= this.addressRepository.findById(request.getAddressId())
            .orElseThrow(()->new RuntimeException(""));
            Order order =new Order();
            order.setAddress(address);
            order.setCustomer(user);
            order.setCreatedAt(new Date());
            this.orderRepository.save(order);
            List<OrderItem> listOrderItems= new ArrayList<>();
            List<CartItem> listCartItems=cart.getListCartItem();
            long totalOrder=0;
            for(CartItem item:listCartItems){
                OrderItem orderItem=new OrderItem();
                orderItem.setFood(item.getFood());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setTotal(item.getTotalPrice());
                orderItem.setOrder(order);
                listOrderItems.add(orderItem);
                totalOrder+= item.getTotalPrice();
            }
            order.setListOrderItems(listOrderItems);
            cart.getListCartItem().clear();
            cart.setTotal(0L);
            this.cartRepository.save(cart);
            Payment payment= this.paymentRepository.findById(request.getPaymentId())
             .orElseThrow(()->new RuntimeException(""));
            StatusOrder statusOrder=this.statusOrderRepository.findByDescription("Chờ xác nhận")
            .orElseThrow(()->new RuntimeException(""));
            if(payment.getName().equals("Tiền mặt")){
                order.setPaid(false);
            }
            else 
            {
                order.setPaid(false);
            }
            order.setPayment(payment);
            order.setStatusOrder(statusOrder);
            order.setFeedback(false);
            order.setTotal(totalOrder);
            this.orderRepository.save(order);
            return order;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }
    public DataResponse getAllOrder(String email,Pageable pageable){
        try {
            User user=this.userRepository.findByEmail(email)
            .orElseThrow(()->new UsernameNotFoundException("Không tìm thấy người dùng!"));
            Page<Order> page = this.orderRepository.findByCustomer(user, pageable);
            List<OrderResponse> list= new ArrayList<>();
            OrderPaginateResponse  paginateResponse = new  OrderPaginateResponse();
                for(Order order:page){
                long total=0;
                OrderResponse orderResponse= new OrderResponse();
                orderResponse.setId(order.getId());
               
               
                List<OrderItemResponse> listOrderItemResponses= new ArrayList<>();
                for(OrderItem item:order.getListOrderItems()){
                    OrderItemResponse orderItemResponse=new OrderItemResponse();
                    orderItemResponse.setName(item.getFood().getName());
                    orderItemResponse.setQuantity(item.getQuantity());
                    orderItemResponse.setFoodId(item.getFood().getId());
                    orderItemResponse.setImages(item.getFood().getImages());
                    orderItemResponse.setTotal(item.getFood().getPrice()*item.getQuantity());
                    
                    total += orderItemResponse.getTotal();
                    listOrderItemResponses.add(orderItemResponse);
                }
                orderResponse.setAddress(order.getAddress());
                orderResponse.setFeedback(order.isFeedback());
                orderResponse.setListOrderItems(listOrderItemResponses);
                orderResponse.setTotal(total);
                orderResponse.setStatusOrder(order.getStatusOrder());
                orderResponse.setCreatedAt(order.getCreatedAt());
                orderResponse.setPaid(order.isPaid());
                list.add(orderResponse);

            }
            paginateResponse.setList(list);
            paginateResponse.setCurrentPage(page.getNumber()+1);
            paginateResponse.setTotalPage(page.getTotalPages());
            return new DataResponse(HttpStatus.OK.value(), null, paginateResponse);
        } catch (Exception e) {
            return new DataResponse(HttpStatus.BAD_REQUEST.value(), null, null);
        }
    }
    public DataResponse cancelOrder(String email,long order_id){
        try {
             User user=this.userRepository.findByEmail(email)
            .orElseThrow(()->new UsernameNotFoundException("Không tìm thấy người dùng!"));
            Order order =this.orderRepository.findById(order_id)
              .orElseThrow(()->new RuntimeException(""));
            if(order.getStatusOrder().getDescription().equals("Đang giao hàng")){
                return new DataResponse(HttpStatus.BAD_REQUEST.value(), "Hủy đơn không thành công do đơn hàng đang được giao đến bạn", null);
            }
            if(order.getStatusOrder().getDescription().equals("Đã giao hàng")){
                return new DataResponse(HttpStatus.BAD_REQUEST.value(), "Hủy đơn không thành công do đơn hàng đã được giao!", null);
            }

            this.orderRepository.deleteById(order_id);
            
            return new DataResponse(HttpStatus.OK.value(), "Hủy đơn hàng thành công!", null);

        } catch (Exception e) {
            // TODO: handle exception
            return new DataResponse(HttpStatus.BAD_REQUEST.value(), "Đã xảy ra lỗi!", null);
        }
    }
    public DataResponse updateStatusOrder(String email,UpdateStatusRequest request){
        try {
              User user=this.userRepository.findByEmail(email)
            .orElseThrow(()->new UsernameNotFoundException("Không tìm thấy người dùng!"));
            Order order =this.orderRepository.findById(request.getOrderId())
              .orElseThrow(()->new RuntimeException(""));
              StatusOrder statusOrder= this.statusOrderRepository.findById(request.getStatusOrderId())
                .orElseThrow(()->new RuntimeException(""));
                order.setStatusOrder(statusOrder);
                this.orderRepository.save(order);
              
                return new DataResponse(HttpStatus.OK.value(), "Cập nhật trạng thái đơn hàng thành công!",null);
        } catch (Exception e) {
            // TODO: handle exception
             return new DataResponse(HttpStatus.BAD_REQUEST.value(), "Đã xảy ra lỗi!", null);
        }
    }
    public DataResponse getAllOrderAdmin(Pageable pageable){
      
        if(pageable != null)
        {
            OrderAdminResponse adminResponse = new  OrderAdminResponse();
            Page<Order> listPage= this.orderRepository.findAll(pageable);
            List<OrderResponse> listOrders= new ArrayList<>();
            
            for(Order order : listPage)
            {
                  List<OrderItemResponse>listOrderItems= new ArrayList<>();
                OrderResponse  orderResponse = new OrderResponse();
               
                for(OrderItem item : order.getListOrderItems())
                {
                     OrderItemResponse  orderItemResponse = new OrderItemResponse();
                    orderItemResponse.setFoodId(item.getFood().getId());
                    orderItemResponse.setImages(item.getFood().getImages());
                    orderItemResponse.setName(item.getFood().getName());
                    orderItemResponse.setQuantity(item.getQuantity());
                    orderItemResponse.setTotal(item.getTotal());
                    listOrderItems.add(orderItemResponse);
                }
                orderResponse.setAddress(order.getAddress());
                orderResponse.setCreatedAt(order.getCreatedAt());
                orderResponse.setFeedback(order.isFeedback());
                orderResponse.setId(order.getId());
                orderResponse.setListOrderItems(listOrderItems);
                orderResponse.setStatusOrder(order.getStatusOrder());
                orderResponse.setTotal(order.getTotal());
                listOrders.add(orderResponse);
                
            }
            adminResponse.setListOrders(listOrders);
            adminResponse.setCurrentPage(listPage.getNumber()+1);
            adminResponse.setTotalPage(listPage.getTotalPages());
            return new DataResponse(HttpStatus.OK.value(),null,adminResponse);
        }
        return null;
    }
    public void updateOrderPaidSuccess(long id)
    {
        Order order = this.orderRepository.findById(id)
        .orElseThrow(()->new RuntimeException("Không tìm thấy đơn hàng"));
        order.setPaid(true);
        this.orderRepository.save(order);
    }
}
