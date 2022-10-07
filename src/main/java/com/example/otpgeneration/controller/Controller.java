package com.example.otpgeneration.controller;

import com.example.otpgeneration.config.OTPConfig;
import com.example.otpgeneration.entity.OtpVerifyEntity;
import com.example.otpgeneration.repository.OtpVerifyRepository;
import com.example.otpgeneration.util.Util;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
class SendMailThreadPool {
    @Bean("Controller.SendMailExecutorService")
    public ExecutorService sendMailThreadPool(){
        return Executors.newCachedThreadPool();
    }
}

@org.springframework.stereotype.Controller
public class Controller {

    private final OtpVerifyRepository otpVerifyRepository;
    private final JavaMailSender javaMailSender;
    private final OTPConfig otpConfig;
    private final ExecutorService executorService;

    public Controller(OtpVerifyRepository otpVerifyRepository,
                      JavaMailSender javaMailSender,
                      OTPConfig otpConfig,
                      @Qualifier("Controller.SendMailExecutorService") ExecutorService executorService) {
        this.otpVerifyRepository = otpVerifyRepository;
        this.javaMailSender = javaMailSender;
        this.otpConfig = otpConfig;
        this.executorService = executorService;
    }

    @GetMapping(path = "/register")
    public String registerGet() {
        return "register";
    }

    @GetMapping(path = "/home")
    public String homeGet() {
        return "success";
    }

    @SneakyThrows
    @PostMapping(path = "/register")
    public String registerPost(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");
        if (!password1.equals(password2)) {
            return "redirect:register?msg=fail";
        }

        if (otpVerifyRepository.findByEmailAndIsActive(email, 1) != null) {
            return "redirect:register?msg=fail";
        }

        final String finalPassword = password1;

        final String otp = Util.generateOTP();

        executorService.submit(() -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@baeldung.com");
            message.setTo(email);
            message.setSubject("[OTP] Register Application");
            message.setText(
                    String.format("Your OTP-register is %s. \n\n This OTP has expirate 1 minutes.", otp)
            );
            javaMailSender.send(message);
        });


        otpVerifyRepository.save(
                OtpVerifyEntity.builder()
                        .email(email)
                        .password(Util.base64Encode(finalPassword))
                        .isActive(0)
                        .timestamp(System.currentTimeMillis())
                        .otp(Util.base64Encode(otp))
                        .build()
        );

        request.setAttribute("email", email);
        return "verify";
    }

    @PostMapping(path = "/otp-verify")
    public String verifyGet(HttpServletRequest request) {
        String email = request.getParameter("email");

        String otp = String.format("%s%s%s%s",
                request.getParameter("number1"),
                request.getParameter("number2"),
                request.getParameter("number3"),
                request.getParameter("number4")
        );

        if (email == null || email.isEmpty()) {
            return "redirect:register?msg=fail";
        }

        OtpVerifyEntity entity = otpVerifyRepository.findByEmailAndIsActive(email, 0);

        if (entity == null || entity.getTimestamp() == null || entity.getOtp() == null) {
            return "redirect:register?msg=fail";
        }


        if (Util.base64Decode(entity.getOtp()).equals(otp)
                && entity.getTimestamp() >= System.currentTimeMillis() - Duration.ofMinutes(otpConfig.getOtpMaxAgeMinute()).toMillis()) {
            entity.setIsActive(1);
            otpVerifyRepository.save(entity);
            return "redirect:home";
        }

        return "redirect:register?msg=fail";
    }


}
