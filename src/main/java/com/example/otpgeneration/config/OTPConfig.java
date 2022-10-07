package com.example.otpgeneration.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class OTPConfig {
    @Value("${system.otp.max-age.minutes}")
    private int otpMaxAgeMinute;
}
