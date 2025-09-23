package com.phv.foodptit.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.phv.foodptit.service.CustomUsersDetailsService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${spring.frontend-url}")
    private String FE_URL;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtTokenValidatorConfig jwtTokenValidatorConfig;
    private final CustomUsersDetailsService customUsersDetailsService;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth->auth.requestMatchers("/api/v1/admin/**").hasAnyRole("ADMIN")
        .requestMatchers("/api/**").authenticated().anyRequest().permitAll()
        )
        .csrf(c->c.disable())
         .cors(cors->cors.configurationSource(configurationSource()))
        .addFilterBefore(jwtTokenValidatorConfig, BasicAuthenticationFilter.class)
        .exceptionHandling(ex->ex.authenticationEntryPoint(authenticationEntryPoint))
        .logout(AbstractHttpConfigurer::disable);
       
       
        return http.build();

    }

    private CorsConfigurationSource configurationSource() {
       return new CorsConfigurationSource() {
        @Override
        public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
           CorsConfiguration cors= new CorsConfiguration();
           cors.setAllowedOrigins(Arrays.asList(FE_URL,"http://localhost:5173"));
           cors.setAllowedMethods(Collections.singletonList("*"));
           cors.setAllowCredentials(true);
           cors.setAllowedHeaders(Collections.singletonList("*"));
           cors.setMaxAge(3600L);
           cors.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie"));
           return cors;
        }
       };
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
//    @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//     return config.getAuthenticationManager();
//     }
    @Bean 
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider authenticationProvider= new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUsersDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authenticationProvider);
    }
}
