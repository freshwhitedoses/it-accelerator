package com.example.pr7.Entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN,
    MODERATOR,
    INVESTOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
