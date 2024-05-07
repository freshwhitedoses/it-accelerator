package com.example.pr7.Entity;

public class UserMapper {
    public static UserDto userToDto(User user) {
        return new UserDto(
                user.getUsernick(),
                user.getName(),
                user.getSurname(),
                user.getTelegramId(),
                user.getEmail(),
                user.getRole(),
                user.getBirthDate()
        );
    }

}
