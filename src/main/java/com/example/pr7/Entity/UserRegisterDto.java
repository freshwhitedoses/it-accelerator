package com.example.pr7.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
    private String name;
    private String surname;
    private String telegramId;
    private String password;
    private String email;
    private Role role;
    private long code;
    private LocalDate birthDate;
}
