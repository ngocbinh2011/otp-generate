package com.example.otpgeneration.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Base64;

@Builder
@Table(name = "otp_verify", uniqueConstraints = {
        @UniqueConstraint(name = "UniqueEmailrAndStatus", columnNames = { "email", "is_active" })})
@Entity
@Data
@AllArgsConstructor
public class OtpVerifyEntity {
    @Id
    @Column(name = "email", columnDefinition = "VARCHAR(100)")
    private String email;

    @Column(name = "password", columnDefinition = "VARCHAR(100)")
    private String password;

    @Column(name = "otp", columnDefinition = "VARCHAR(100)")
    private String otp;

    @Column(name = "is_active", columnDefinition = "TINYINT(1)")
    private Integer isActive;

    @Column(name = "timestamp")
    private Long timestamp;

    public OtpVerifyEntity() {

    }

}
