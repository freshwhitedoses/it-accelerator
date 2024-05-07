package com.example.pr7.Service;

import com.example.pr7.Entity.EmailCode;
import com.example.pr7.Repository.EmailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

@Transactional
@Slf4j
@Service
public class EmailService {
    private JavaMailSender javaMailSender;
    private EmailRepository emailRepository;
    private String messageFrom;

    public EmailService(JavaMailSender javaMailSender, EmailRepository emailRepository, @Value("${spring.mail.username}") String messageFrom) {
        this.javaMailSender = javaMailSender;
        this.emailRepository = emailRepository;
        this.messageFrom = messageFrom;
    }
    Optional<EmailCode> getCode (String email) {
        return this.emailRepository.getEmailCodeByEmail(email);
    }
    void deleteCode(String email) {
        this.emailRepository.deleteEmailCodeByEmail(email);
    }
    public void sendMail(String email) {
        System.out.println("start");
        this.emailRepository.deleteEmailCodeByEmail(email);
        System.out.println("start2");

        int code = this.generateCode();
        this.emailRepository.save(new EmailCode(email, code));
        SimpleMailMessage message = new SimpleMailMessage();
        System.out.println("start3");

        message.setFrom(this.messageFrom);
        message.setTo(email);
        message.setSubject("Дзынь, дзынь, подтверди почту");
        System.out.println("start4");
        try {
            message.setText(String.valueOf(code));
            javaMailSender.send(message);
        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("start5");
        }

    }
    public int generateCode() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        return random.nextInt(max - min + 1) + min;

    }
}
