package com.example.pr7.Controller;

import com.example.pr7.Entity.*;
import com.example.pr7.Service.TeamService;
import com.example.pr7.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class TeamController {
    private final UserService userService;
    private final TeamService teamService;

    public TeamController(UserService userService, TeamService teamService) {
        this.userService = userService;
        this.teamService = teamService;
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER', 'INVESTOR')")
    @PostMapping("/team")
    public ResponseEntity<Response> createTeam(@RequestBody UsersForTeam userNames) {
        UUID teamId = this.teamService.createTeam(userNames.getUserNames(), userNames.getName());
        if (teamId == null) {
            return new ResponseEntity<>(new Response<>(false, "Произошла ошибка", null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Response<>(true, "Команда успешно создана", new TeamResponse(teamId)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER', 'INVESTOR')")
    @GetMapping("/teams_by_user")
    public ResponseEntity<Response> getTeamByUserEmail() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Team> teams = this.teamService.getTeamsByUserEmail(user.getEmail());

        if (teams == null) {
            return new ResponseEntity<>(new Response<>(false, "Произошла ошибка", null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Response<>(true, "OK", new TeamsResponse(teams)), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER', 'INVESTOR')")
    @GetMapping("/teams_by_user_id")
    public ResponseEntity<Response> getTeamByUserId() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Team> teams = this.teamService.getTeamsByUser(user.getUserId());

        if (teams == null) {
            return new ResponseEntity<>(new Response<>(false, "Произошла ошибка", null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Response<>(true, "OK", new TeamsResponse(teams)), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("/team/feedback")
    public ResponseEntity<Response> sendFeedbackFromTeam(@RequestBody TeamFeedback teamFeedback) {
        InvestingStartupDto startup = this.teamService.sendFeedbackFromTeam(teamFeedback);
        if (startup == null) {
            return new ResponseEntity<>(new Response<>(false, "Произошла ошибка", null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Response<>(true, "OK", startup), HttpStatus.OK);

    }

}
