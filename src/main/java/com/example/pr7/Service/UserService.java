package com.example.pr7.Service;

import com.example.pr7.Config.SecurityConfig;
import com.example.pr7.Entity.*;
import com.example.pr7.Repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TeamRepository teamRepository;
    private final StartupRepository startupRepository;
    private final InvestingStartupRepository investingStartupRepository;

    public UserService(UserRepository userRepository, SecurityConfig securityConfig, EmailService emailService, TeamRepository teamRepository, StartupRepository startupRepository, InvestingStartupRepository investingStartupRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.teamRepository = teamRepository;
        this.startupRepository = startupRepository;
        this.investingStartupRepository = investingStartupRepository;
    }
    public boolean isValidUser(UserLoginDto user) {
        Optional<User> user1 = userRepository.findUserByEmail(user.getEmail());
        if(user1.isEmpty()) return false;
        if ( !this.emailService.getCode(user.getEmail()).isPresent()
                || this.emailService.getCode(user.getEmail()).get().getCode() != user.getCode()
        ) return false;
        System.out.println(user1.get().getPassword());
        System.out.println(SecurityConfig.encoder().matches(user.getPassword(), user1.get().getPassword()));
        return user1.filter(value -> SecurityConfig.encoder().matches(user.getPassword(), value.getPassword())).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.getUserByEmail(email);
    }

    public boolean addUser(UserRegisterDto user) {
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()
            || !this.emailService.getCode(user.getEmail()).isPresent()
                || this.emailService.getCode(user.getEmail()).get().getCode() != user.getCode()
        )
        {
            System.out.println("check");
            return false;
        }
        User userToRepo = new User();
        UUID userId = generateUniqueUUID();
        String username = createUserName(userId);
        userToRepo.setUserId(userId);
        userToRepo.setUsernick(username);
        userToRepo.setRole(user.getRole());
        userToRepo.setName(user.getName());
        userToRepo.setSurname(user.getSurname());
        userToRepo.setTelegramId(user.getTelegramId());
        userToRepo.setBirthDate(user.getBirthDate());
        userToRepo.setEmail(user.getEmail());
        String encodedPassword = SecurityConfig.encoder().encode(user.getPassword());
        userToRepo.setPassword(encodedPassword);
        userRepository.save(userToRepo);
        return true;
    }
    public boolean registerUser(User user) {
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            return false;
        }
        String encodedPassword = SecurityConfig.encoder().encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return true;
    }
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }
    public InvestingStartupDto setInvestorFeedback(UUID investorId, InvestorFeedback investorFeedback) {
        UUID investingStartupId = generateUniqueUUID();
        InvestingStartup startup = new InvestingStartup(investingStartupId, investorFeedback.getStartupId(), investorId, investorFeedback.getStatus(), 0);
        this.investingStartupRepository.save(startup);
        InvestingStartupDto dto = new InvestingStartupDto(
                startup.getInvestingStartupId(),
                this.startupRepository.getStartupByStartupId(startup.getStartupId()),
                this.userRepository.getUserByUserId(startup.getInvestorId()),
                startup.getTeamStatus(), startup.getInvestorStatus());
        return dto;
    }
    //TODO: переделать метод а может и не надо
    public Startup sendFeedbackFromAnalyst(AnalystFeedback analystFeedback, UUID userId) {
        Startup startup = startupRepository.getStartupByStartupId(analystFeedback.getStartupId());
        startup.setAnalystId(userId);
        startup.setAnalystComment(analystFeedback.getAnalystComment());
        startup.setStatus(analystFeedback.getStatus());
        this.startupRepository.save(startup);
        return startup;
    }
    public String createUserName(UUID id) {
        long uuidMostSignificantBits = id.getMostSignificantBits();
        long uuidLeastSignificantBits = id.getLeastSignificantBits();
        long combinedBits = uuidMostSignificantBits ^ uuidLeastSignificantBits;
        long result =  Math.abs(combinedBits % (long) Math.pow(10, 10));
        return String.valueOf(result);
    }
    public static UUID generateUniqueUUID() {
        return UUID.randomUUID();
    }

}
