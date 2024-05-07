package com.example.pr7.Service;

import com.example.pr7.Entity.*;
import com.example.pr7.Repository.InvestingStartupRepository;
import com.example.pr7.Repository.StartupRepository;
import com.example.pr7.Repository.TeamRepository;
import com.example.pr7.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Transactional
@Slf4j
@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final InvestingStartupRepository investingStartupRepository;
    private final StartupRepository startupRepository;



    public TeamService(TeamRepository teamRepository, UserRepository userRepository, InvestingStartupRepository investingStartupRepository, StartupRepository startupRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.investingStartupRepository = investingStartupRepository;
        this.startupRepository = startupRepository;
    }
    public UUID createTeam(List<String> users, String name) {
        List<User> usersIds = new ArrayList<>();
        users.forEach(u -> {
            User user = this.userRepository.findUserByUsernick(u).isPresent()
                    ? this.userRepository.findUserByUsernick(u).get() : null;
            if (user!=null) usersIds.add(user);
        });
        UUID teamId = generateUniqueUUID();
        this.teamRepository.save(new Team(teamId, name, usersIds));
        return teamId;
    }
    public List<Team> getTeamsByUser(UUID userId) {
        return this.teamRepository.findAllByUsersUserId(userId);
    }
    public List<Team> getTeamsByUserEmail(String email) {
        User user = this.userRepository.getUserByEmail(email);
        return this.teamRepository.findAllByUsersUserId(user.getUserId());
    }
    public InvestingStartupDto sendFeedbackFromTeam(TeamFeedback teamFeedback) {
        InvestingStartup startup = this.investingStartupRepository.findInvestingStartupByInvestingStartupId(teamFeedback.getInvestingStartupId());
        startup.setTeamStatus(teamFeedback.getStatus());
        this.investingStartupRepository.save(startup);
        InvestingStartupDto dto = new InvestingStartupDto(
                startup.getInvestingStartupId(),
                this.startupRepository.getStartupByStartupId(startup.getStartupId()),
                this.userRepository.getUserByUserId(startup.getInvestorId()),
                startup.getTeamStatus(), startup.getInvestorStatus());
        return dto;
    }
    public static UUID generateUniqueUUID() {
        return UUID.randomUUID();
    }

}
