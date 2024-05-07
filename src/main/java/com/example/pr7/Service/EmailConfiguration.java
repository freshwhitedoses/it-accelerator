package com.example.pr7.Service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "email")
public class EmailConfiguration {
    String username;
    String password;
    String server;
    int port;
}
