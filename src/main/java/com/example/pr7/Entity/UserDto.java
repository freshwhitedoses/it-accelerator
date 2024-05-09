package com.example.pr7.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
    private LocalDate birthDate;
}
