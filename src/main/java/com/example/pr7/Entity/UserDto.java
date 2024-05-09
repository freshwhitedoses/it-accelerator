package com.example.pr7.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserDto {
    private String username;
    private String name;
    private String surname;
    private String telegramId;
    private String email;
    private Role role;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;
}
