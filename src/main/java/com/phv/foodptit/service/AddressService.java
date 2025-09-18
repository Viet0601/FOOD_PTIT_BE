package com.phv.foodptit.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phv.foodptit.entity.Address;
import com.phv.foodptit.entity.User;
import com.phv.foodptit.entity.User_Address;
import com.phv.foodptit.entity.DTO.AddressRequest;
import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.repository.AddressRepository;
import com.phv.foodptit.repository.UserRepository;
import com.phv.foodptit.repository.User_AddressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final User_AddressRepository user_AddressRepository;
    @Transactional
    public DataResponse createNewAddress(String email,AddressRequest request){
        DataResponse response=new DataResponse();
        try {
        User user= this.userRepository.findByEmail(email)
        .orElseThrow(()->new UsernameNotFoundException("Không tìm thấy người dùng!"));
        Address address= new Address();
        address.setDetail(request.getDetail());
        address.setDistrict(request.getDistrict());
        address.setProvince(request.getProvince());
        address.setVillage(request.getVillage());
        address.setPhone(request.getPhone());
        this.addressRepository.save(address);
        User_Address user_Address= new User_Address();
        user_Address.setAddress(address);
        user_Address.setCustomer(user);
        this.user_AddressRepository.save(user_Address);
        List<User_Address>listAddresses=user.getListAddresses();
        listAddresses.add(user_Address);
        List<Address>list=new ArrayList<>();
        for (User_Address user_address:listAddresses){
            list.add(user_address.getAddress());
        }
        
        this.userRepository.save(user);
        response.setEc(HttpStatus.SC_CREATED);
        response.setEm("Thêm địa chỉ thành công!");
        response.setDt(list);
        } catch (Exception e) {
            // TODO: handle exception
             response.setEc(HttpStatus.SC_BAD_REQUEST);
        }
        return response;
      
    }
    @Transactional
    public DataResponse UpdateAddress(long address_id,String email,AddressRequest request){
        DataResponse response=new DataResponse();
        try {
             User user= this.userRepository.findByEmail(email)
        .orElseThrow(()->new UsernameNotFoundException("Không tìm thấy người dùng!"));
        Address address= this.addressRepository.findById(address_id)
        .orElseThrow(()->new RuntimeException("Không tìm thấy địa chỉ"));
        User_Address user_Address= this.user_AddressRepository.findByCustomerAndAddress(user, address)
        .orElseThrow(()->new RuntimeException("Không tìm thấy địa chỉ người dùng!"));
        Address updateAddress= user_Address.getAddress();
        updateAddress.setDetail(request.getDetail());
        updateAddress.setDistrict(request.getDistrict());
        updateAddress.setProvince(request.getProvince());
        updateAddress.setVillage(request.getVillage());
        updateAddress.setPhone(request.getPhone());
        user_Address.setAddress(updateAddress);
        this.user_AddressRepository.save(user_Address);
        response.setEc(HttpStatus.SC_OK);
        response.setEm("Cập nhật địa chỉ thành công!");
        } catch (Exception e) {
            // TODO: handle exception
            response.setEc(HttpStatus.SC_BAD_REQUEST);
        response.setEm("Đã xảy ra lỗi!");
      
        }
       
        return response;
    }
    @Transactional
    public DataResponse DeteleAddress(long address_id,String email){
        DataResponse response=new DataResponse();
        try {
             User user= this.userRepository.findByEmail(email)
        .orElseThrow(()->new UsernameNotFoundException("Không tìm thấy người dùng!"));
        Address address= this.addressRepository.findById(address_id)
        .orElseThrow(()->new RuntimeException("Không tìm thấy địa chỉ"));
        User_Address user_Address= this.user_AddressRepository.findByCustomerAndAddress(user, address)
        .orElseThrow(
            ()->new RuntimeException("Không tìm thấy địa chỉ người dùng!"));
    
        this.user_AddressRepository.delete(user_Address);
        List<User_Address>listAddresses=user.getListAddresses();
        List<Address>list=new ArrayList<>();
        for(User_Address ua:listAddresses)
        {
            list.add(ua.getAddress());
        }
        response.setEc(HttpStatus.SC_OK);
        response.setDt(list);
        } catch (Exception e) {
            // TODO: handle exception
         response.setEc(HttpStatus.SC_BAD_REQUEST);
        response.setEm("Đã xảy ra lỗi!");
        e.printStackTrace();
        }
       
        return response;
    }
    public DataResponse getAllAddress(String email){
        try {
            User user= this.userRepository.findByEmail(email)
        .orElseThrow(()->new UsernameNotFoundException("Không tìm thấy người dùng!"));
        List<User_Address>listAddresses= this.user_AddressRepository.findByCustomer(user);
        List<Address> list= new ArrayList<>();
        for(User_Address address:listAddresses){
            list.add(address.getAddress());
        }
        return new DataResponse(HttpStatus.SC_OK, null, list);
        } catch (Exception e) {
            // TODO: handle exception
            return new DataResponse(HttpStatus.SC_BAD_REQUEST, null, null);
        }
    }
}
