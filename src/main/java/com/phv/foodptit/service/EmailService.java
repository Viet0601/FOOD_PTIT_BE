package com.phv.foodptit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.properties.mail.smtp.from}")
    private String emailSender;
    
    private final JavaMailSender javaMailSender;
    public void sendWelComeMail(String email,String name){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailSender);
        message.setTo(email);
        message.setSubject("Chào mừng đến với PTIT FOOD");
        message.setText("Xin chào "+name+",\n\nCảm ơn vì đã đăng kí tại khoản Hãy khám phá những món ngon ngay nào!\n\nTrân trọng,\nPTIT SHOP");
        javaMailSender.send(message);
        
    }
    public void sendResetPassword(String email,long otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailSender);
        message.setTo(email);
        message.setSubject("Dặt lại mật khẩu");
        message.setText("Xin chào "+email+",\n\nMã OTP của bạn là: "+String.valueOf(otp)+". Mã sẽ có hiệu lực trong vòng 5 phút!");
        javaMailSender.send(message);
    }
}
