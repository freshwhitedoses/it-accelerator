package com.example.pr7.Controller;

import com.example.pr7.Entity.CodeDto;
import com.example.pr7.Entity.Response;
import com.example.pr7.Entity.User;
import com.example.pr7.Service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }
    @PostMapping("/send_email")
    public ResponseEntity<Response> sendEmailCode(@RequestBody CodeDto email) {
        this.emailService.sendMail(email.getEmail());
        return ResponseEntity.ok(new Response(true, "Код отправлен на почту", null));
    }
}