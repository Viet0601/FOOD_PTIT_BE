package com.phv.foodptit.config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.phv.foodptit.service.CustomUsersDetailsService;
import com.phv.foodptit.service.JwtService;
import com.phv.foodptit.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JwtTokenValidatorConfig extends OncePerRequestFilter {

    
    private final JwtService jwtService;
    private final CustomUsersDetailsService customUsersDetailsService;
    @Value("${jwt-header}")
    private  String jwt_header;
    @Value("${jwt.secret-key}")
    private  String jwt_secret;
    private static final List<String>PUBLIC_URL=List.of("/login","/register");
   
    @Override 
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                String path= request.getServletPath();
                if(this.PUBLIC_URL.contains(path))
                {
                    filterChain.doFilter(request, response);
                    return;
                }
               try {
                 String token=null;
                String email=null;
                // check token in header
                String authorizationHeader=request.getHeader("Authorization");
                if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
                    token= authorizationHeader.substring(7);
                }
                // if not found in header, check cookie
                if(token ==null)
                {
                    Cookie[]cookies=request.getCookies();
                    if(cookies!=null)
                    {
                          for (Cookie cookie:cookies)
                        {
                         if(cookie.getName().equals("token")){
                            token = cookie.getValue();
                            break;
                         }
                        }
                    }
                }
                if(token!=null)
                {
                    email=this.jwtService.getEmail(token);
                    if(email !=null && SecurityContextHolder.getContext().getAuthentication()==null){
                        UserDetails userDetails = this.customUsersDetailsService.loadUserByUsername(email);
                        if(this.jwtService.isValidateToken(token, userDetails)){
                            UsernamePasswordAuthenticationToken authenticationToken=
                            new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                       
                    }

                }
                filterChain.doFilter(request, response);
               } catch (Exception e) {
                // TODO: handle exception
                throw new BadCredentialsException("JWT expired nhe", e);
               }

            }
    
}
