package com.example.pr7.Controller;

import com.example.pr7.Entity.*;
import com.example.pr7.Service.StartupService;
import com.example.pr7.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StartupController {
    private final UserService userService;
    private final StartupService startupService;

    public StartupController(UserService userService, StartupService startupService) {
        this.userService = userService;
        this.startupService = startupService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER', 'INVESTOR')")
    @PostMapping("/startup")
    public ResponseEntity<Response> createStartup(@RequestBody StartupRequest startup) {
        Startup startup1 = this.startupService.createStartup(startup);
        if(startup1 == null) {
            return new ResponseEntity<>(new Response<>(false, "Произошла ошибка", null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Response<>(true, "Стартап успешно создан", startup1),HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @PutMapping("/feedback")
    public ResponseEntity<Response> createStartup(@RequestBody AnalystFeedback analystFeedback) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Startup startup1 = this.userService.sendFeedbackFromAnalyst(analystFeedback, user.getUserId());
        if(startup1 == null) {
            return new ResponseEntity<>(new Response<>(false, "Произошла ошибка", null),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Response<>(true, "Стартап успешно изменен", startup1),HttpStatus.OK);
    }
}
