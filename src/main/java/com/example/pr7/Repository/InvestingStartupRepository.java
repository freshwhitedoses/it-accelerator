package com.example.pr7.Repository;

import com.example.pr7.Entity.InvestingStartup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InvestingStartupRepository extends JpaRepository<InvestingStartup, UUID> {
    public InvestingStartup findInvestingStartupByInvestingStartupId(UUID investingStartupId);

    public List<InvestingStartup> findInvestingStartupsByStartupIdAndInvestorStatusAndTeamStatus(UUID startupId, int status, int teamStatus);

    public List<InvestingStartup> findInvestingStartupsByInvestorIdAndInvestorStatus(UUID investorId, int status);
}
