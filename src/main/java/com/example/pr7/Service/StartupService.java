package com.example.pr7.Service;

import com.example.pr7.Entity.InvestingStartup;
import com.example.pr7.Entity.InvestingStartupDto;
import com.example.pr7.Entity.Startup;
import com.example.pr7.Entity.StartupRequest;
import com.example.pr7.Repository.InvestingStartupRepository;
import com.example.pr7.Repository.StartupRepository;
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
public class StartupService {
    private final StartupRepository startupRepository;
    private final UserRepository userRepository;

    private final InvestingStartupRepository investingStartupRepository;

    public StartupService(StartupRepository startupRepository, UserRepository userRepository, InvestingStartupRepository investingStartupRepository) {
        this.startupRepository = startupRepository;
        this.userRepository = userRepository;
        this.investingStartupRepository = investingStartupRepository;
    }


    public Startup createStartup(StartupRequest startupRequest) {
        UUID startupId = generateUniqueUUID();
        Startup startup = new Startup(startupRequest);
        startup.setStartupId(startupId);
        this.startupRepository.save(startup);
        return startup;
    }
    public Startup editStartup(Startup startup) {
        this.startupRepository.save(startup);
        return startup;
    }
    public void deleteStartup(UUID startup) {
        this.startupRepository.deleteStartupByStartupId(startup);
    }
    public static UUID generateUniqueUUID() {
        return UUID.randomUUID();
    }
    public List<Startup> getStartupsByAnalystStatus(int status) {
        return this.startupRepository.getStartupsByStatus(status);
    }
    public List<Startup> getStartupByTeam(UUID team) {
        return this.startupRepository.getStartupsByTeamId(team);
    }
    public List<Startup> getStartupByTeamAndStatus(UUID team, int status) {
        return this.startupRepository.getStartupsByTeamIdAndStatus(team, status);
    }
    public List<InvestingStartupDto> getStartupsByInvestorsFeedback(UUID startupId, int teamStatus) {
        List<InvestingStartup> startups = this.investingStartupRepository.findInvestingStartupsByStartupIdAndInvestorStatusAndTeamStatus(startupId, 1, teamStatus);
        List<InvestingStartupDto> startupDtos = new ArrayList<>();
        startups.forEach(it -> {
            InvestingStartupDto dto = new InvestingStartupDto(
                    it.getInvestingStartupId(),
                    this.startupRepository.getStartupByStartupId(it.getStartupId()),
                    this.userRepository.getUserByUserId(it.getInvestorId()),
                    it.getTeamStatus(), it.getInvestorStatus());
            startupDtos.add(dto);
        });
        return startupDtos;
    }
    public List<InvestingStartupDto> getStartupsByMatchedInvestorId(UUID investorId) {
        List<InvestingStartup> startups = this.investingStartupRepository.findInvestingStartupsByInvestorIdAndInvestorStatus(investorId, 1);
        List<InvestingStartupDto> startupDtos = new ArrayList<>();
        startups.forEach(it -> {
            InvestingStartupDto dto = new InvestingStartupDto(
                    it.getInvestingStartupId(),
                    this.startupRepository.getStartupByStartupId(it.getStartupId()),
                    this.userRepository.getUserByUserId(it.getInvestorId()),
                    it.getTeamStatus(), it.getInvestorStatus());
            startupDtos.add(dto);
        });
        return startupDtos;
    }
    public List<Startup> getStartupsByAnalystStatusAndFilters(Integer mainInStartup, Integer mainInMe, Integer maturityStage) {
        return this.startupRepository.getStartupsByFilters(1,mainInStartup, mainInMe,maturityStage);
    }
}
