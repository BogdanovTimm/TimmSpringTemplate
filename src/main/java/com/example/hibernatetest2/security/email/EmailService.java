package com.example.hibernatetest2.security.email;

import com.example.hibernatetest2.security.entities.VerificationType;

public interface EmailService {
    void sendVerificationEmail(String firstName, String email, String verificationUrl, VerificationType verificationType);
}
