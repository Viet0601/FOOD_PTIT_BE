package com.phv.foodptit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.phv.foodptit.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUsersDetailsService implements UserDetailsService  {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(">>> loadUserByUsername called with: " + username);
        com.phv.foodptit.entity.User user = this.userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("User not found!"));

      
        List<GrantedAuthority> auth=new ArrayList<>();
        auth.add(new SimpleGrantedAuthority("ROLE_"+user.getRole().getName().toString()));
         System.out.println(">>> Login with email: " + user.getEmail());
        System.out.println(">>> Password in DB: " + user.getPassword());
        return new User(user.getEmail(),user.getPassword(),auth);
    }
    
}
