package com.example.pr7.Controller;

import com.example.pr7.Config.JWTGenerator;
import com.example.pr7.Entity.*;
import com.example.pr7.Service.StartupService;
import com.example.pr7.Service.TeamService;
import com.example.pr7.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    private final UserService userService;
    private final TeamService teamService;
    private final StartupService startupService;


    private final JWTGenerator _jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;

    public UserController(UserService userService, TeamService teamService, StartupService startupService, JWTGenerator jwtProvider, AuthenticationManager authenticationManager, JWTGenerator jwtGenerator) {
        this.userService = userService;
        this.teamService = teamService;
        this.startupService = startupService;
        _jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/signup")
    public ResponseEntity<Response> addUser(@RequestBody UserRegisterDto user) {
        if (!userService.addUser(user))
            return new ResponseEntity<>(new Response<>(false, "Такой пользователь уже существует", null), HttpStatus.BAD_REQUEST);

        else {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);
            return new ResponseEntity<>(new Response<>(true, "Регистрация прошла успешно", new AuthResponseDTO(token)), HttpStatus.OK);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody UserLoginDto user) {
        if (userService.isValidUser(user)) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);
            return new ResponseEntity<>(new Response<>(true, "Регистрация прошла успешно", new AuthResponseDTO(token)), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Response<>(false, "Такой пользователь уже существует", null), HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/account")
    public ResponseEntity<Response> getCurrentAccount(@RequestHeader("Authorization") String token) {
        System.out.println(token);
        return new ResponseEntity<>(new Response<>(true, "OK", UserMapper.userToDto((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal())), HttpStatus.OK);
    }

}