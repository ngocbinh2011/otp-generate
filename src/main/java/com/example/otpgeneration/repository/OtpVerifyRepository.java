package com.example.otpgeneration.repository;

import com.example.otpgeneration.entity.OtpVerifyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OtpVerifyRepository extends JpaRepository<OtpVerifyEntity, String> {
    OtpVerifyEntity findByEmailAndIsActive(String email, Integer isActive);
}
