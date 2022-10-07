package com.example.otpgeneration.util;

import java.util.Base64;
import java.util.Random;

public class Util {
    private static Random random = new Random();

    public static String generateOTP() {
        return String.format("%04d", random.nextInt(10000));
    }

    public static String base64Encode(String data){
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    public static String base64Decode(String data){
        return new String(Base64.getDecoder().decode(data));
    }

}
