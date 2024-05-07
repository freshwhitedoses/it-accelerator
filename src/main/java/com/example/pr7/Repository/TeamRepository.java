package com.example.pr7.Repository;

import com.example.pr7.Entity.Team;
import com.example.pr7.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public interface TeamRepository extends JpaRepository<Team, UUID> {
    public List<Team> findAllByUsersUserId(UUID userId);
}
