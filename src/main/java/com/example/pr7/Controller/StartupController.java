package com.example.pr7.Controller;

import com.example.pr7.Entity.*;
import com.example.pr7.Service.StartupService;
import com.example.pr7.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @PutMapping("/analyst/feedback")
    public ResponseEntity<Response> sendFeedbackFromAnalyst(@RequestBody AnalystFeedback analystFeedback) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Startup startup = this.userService.sendFeedbackFromAnalyst(analystFeedback, user.getUserId());
        if (startup == null) {
            return new ResponseEntity<>(new Response<>(false, "Произошла ошибка", null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Response<>(true, "OK", startup), HttpStatus.OK);

    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/investor/feedback")
    public ResponseEntity<Response> sendFeedbackFromInvestor(@RequestBody InvestorFeedback investorFeedback) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InvestingStartupDto startup = this.userService.setInvestorFeedback( user.getUserId(), investorFeedback);
        if (startup == null) {
            return new ResponseEntity<>(new Response<>(false, "Произошла ошибка", null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Response<>(true, "OK", startup), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("/startup")
    public ResponseEntity<Response> editStartup(@RequestBody Startup startup) {
        Startup newStartup = this.startupService.editStartup(startup);
        if (newStartup == null) {
            return new ResponseEntity<>(new Response<>(false, "Произошла ошибка", null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Response<>(true, "OK", newStartup), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping("/startup")
    public ResponseEntity<Response> deleteStartup(@RequestParam UUID id) {
        boolean error = false;
        try {
            this.startupService.deleteStartup(id);
        } catch (Exception e) {
            error = true;
        }
        if (error) {
            return new ResponseEntity<>(new Response<>(false, "Произошла ошибка", null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Response<>(true, "OK", null), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'INVESTOR')")
    @GetMapping("/startups/investor")
    public ResponseEntity<Response> getStartupForInvestor(@RequestParam int mainInStartup, @RequestParam int mainInMe, @RequestParam int maturityStage) {
        List<Startup> startupList = startupService.getStartupsByAnalystStatusAndFilters(mainInStartup, mainInMe, maturityStage);
        if (startupList == null) {
            return new ResponseEntity<>(new Response<>(false, "Произошла ошибка", null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Response<>(true, "OK", startupList), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @GetMapping("/startups/moderator")
    public ResponseEntity<Response> getStartupForModerator(@RequestParam int status) {
        List<Startup> startupList = startupService.getStartupsByAnalystStatus(status);
        if (startupList == null) {
            return new ResponseEntity<>(new Response<>(false, "Произошла ошибка", null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Response<>(true, "OK", startupList), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/startups/team")
    public ResponseEntity<Response> getStartupForTeam(@RequestParam UUID teamId) {
        List<Startup> startupList = startupService.getStartupByTeam(teamId);
        if (startupList == null) {
            return new ResponseEntity<>(new Response<>(false, "Произошла ошибка", null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Response<>(true, "OK", startupList), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/startups/team/by_status")
    public ResponseEntity<Response> getStartupForTeamByStatus(@RequestParam UUID teamId, @RequestParam int status) {
        List<Startup> startupList = startupService.getStartupByTeamAndStatus(teamId, status);
        if (startupList == null) {
            return new ResponseEntity<>(new Response<>(false, "Произошла ошибка", null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Response<>(true, "OK", startupList), HttpStatus.OK);
    }
//TODO: метч/не метч для команды

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/startups/team/by_investor")
    public ResponseEntity<Response> getStartupForTeamByInvestorsFeedback(@RequestParam UUID startupId, @RequestParam int teamStatus) {
        List<InvestingStartupDto> startupList = startupService.getStartupsByInvestorsFeedback(startupId, teamStatus);
        if (startupList == null) {
            return new ResponseEntity<>(new Response<>(false, "Произошла ошибка", null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Response<>(true, "OK", startupList), HttpStatus.OK);
    }
    //TODO: метч/не метч для инвестора

    @PreAuthorize("hasAnyAuthority('ADMIN', 'INVESTOR')")
    @GetMapping("/startups/match")
    public ResponseEntity<Response> getStartupsByMatchedInvestorId() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<InvestingStartupDto> startupList = startupService.getStartupsByMatchedInvestorId(user.getUserId());
        if (startupList == null) {
            return new ResponseEntity<>(new Response<>(false, "Произошла ошибка", null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Response<>(true, "OK", startupList), HttpStatus.OK);
    }
}
