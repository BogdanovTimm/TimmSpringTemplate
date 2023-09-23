package com.example.hibernatetest2.security.email;

import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.hibernatetest2.security.entities.VerificationType;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;




    @Override
    public void sendVerificationEmail(String firstName,
                                      String email,
                                      String verificationUrl,
                                      VerificationType verificationType) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            log.info("1");
            message.setFrom("bogdanovtimm@gmail.com"); // Email from which you will send emails
            log.info("2");
            message.setTo(email);
            log.info("3");
            message.setText(getEmailMessage(firstName,
                                            verificationUrl,
                                            verificationType));
            log.info("4");
            message.setSubject(String.format("SecureCapita - %s Verification Email",
                                             StringUtils.capitalize(verificationType.getType())));
            log.info("5");
            mailSender.send(message);
            log.info("Email sent to {}", firstName);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    private String getEmailMessage(String firstName, String verificationUrl, VerificationType verificationType) {
        switch (verificationType) {
            case PASSWORD -> {
                return "Hello " + firstName + "\n\nReset password request. Please click the link below to reset your password. \n\n" + verificationUrl + "\n\nThe Support Team";
            }
            case ACCOUNT -> {
                return "Hello " + firstName + "\n\nYour new account has been created. Please click the link below to verify your account. \n\n" + verificationUrl + "\n\nThe Support Team";
            }
            default -> throw new RuntimeException("Unable to send email. Email type unknown");
        }
    }
}