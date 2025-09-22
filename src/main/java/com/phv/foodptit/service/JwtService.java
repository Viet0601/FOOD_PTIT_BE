package com.phv.foodptit.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JwtService {
    @Value("${jwt.secret-key}")
    private  String secret_key;

    public String generateToken(Authentication auth){
        SecretKey key= Keys.hmacShaKeyFor(secret_key.getBytes());
        Collection < ? extends GrantedAuthority> auCollections= auth.getAuthorities();
        String role= populateAuthorities(auCollections);
        return Jwts.builder().setIssuedAt(new Date())
        .setExpiration(new Date(new Date().getTime() + 30*60*1000))
        .claim("email",auth.getName())
        .claim("authorities",role)
        .signWith(key,SignatureAlgorithm.HS256)
        .compact();
    
    }
    public String generateRefeshToken(Authentication auth){
        SecretKey key= Keys.hmacShaKeyFor(secret_key.getBytes());
        Collection < ? extends GrantedAuthority> auCollections= auth.getAuthorities();
        String role= populateAuthorities(auCollections);
        return Jwts.builder().setIssuedAt(new Date())
        .setExpiration(new Date(new Date().getTime() + 7*86400*1000))
        .claim("email",auth.getName())
        .claim("authorities",role)
        .signWith(key,SignatureAlgorithm.HS256)
        .compact();
    
    }
    public String refreshAccessToken(String refreshToken) {
        try {
             SecretKey key= Keys.hmacShaKeyFor(secret_key.getBytes());
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            String email = claims.get("email", String.class);
            String role = claims.get("authorities",String.class);
            if (email == null) return null;

            // Nếu refresh token còn sống → cấp access token mới
            return Jwts.builder()
                    .setSubject(email)
                    .claim("email", email)
                    .claim("authorities", role)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 30*60*1000))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

        } catch (ExpiredJwtException e) {
            // refresh token cũng hết hạn => bắt user login lại
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    public String getEmail(String token){
        try {
        SecretKey key = Keys.hmacShaKeyFor(secret_key.getBytes());
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return String.valueOf(claims.get("email"));
    } catch (ExpiredJwtException e) {
        // Token hết hạn
        return null;
    } catch (Exception e) {
        // Token sai định dạng hoặc không hợp lệ
        return null;
    }
    }
    private Date getExpirationToken(String token){
         SecretKey key= Keys.hmacShaKeyFor(secret_key.getBytes());
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getExpiration();
    }
    public boolean isValidateToken(String token, UserDetails userDetails) {
    try {
        String email = this.getEmail(token);
        return !getExpirationToken(token).before(new Date()) && email.equals(userDetails.getUsername());
    } catch (ExpiredJwtException e) {
        throw e; // ném ra để filter/entrypoint xử lý
    }
}
    private String populateAuthorities(Collection < ? extends GrantedAuthority> auCollections){
        Set<String>listAuth= new HashSet<>();
        for(GrantedAuthority grandauth: auCollections){
            listAuth.add(grandauth.getAuthority());
        }
        return String.join(",", listAuth);

    }
}
