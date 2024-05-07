package com.example.pr7.Repository;

import com.example.pr7.Entity.EmailCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component

public interface EmailRepository extends JpaRepository<EmailCode, String> {
    public Optional<EmailCode> getEmailCodeByEmail(String email);
    public void deleteEmailCodeByEmail(String email);
}
