package com.example.pr7.Repository;

import com.example.pr7.Entity.Startup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public interface StartupRepository extends JpaRepository<Startup, UUID> {
    Startup getStartupByStartupId(UUID startupId);
    void deleteStartupByStartupId(UUID startupId);
    List<Startup> getStartupsByStatus(int status);

    List<Startup> getStartupsByTeamId(UUID teamId);
    List<Startup> getStartupsByTeamIdAndStatus(UUID teamId, int status);

    @Query("SELECT st FROM Startup st WHERE (:status IS NULL OR st.status = :status) AND (:mainInMe IS NULL OR :mainInMe = st.mainInMe)AND (:mainInStartup IS NULL OR :mainInStartup = st.mainInStartup)AND (:maturityStage IS NULL OR :maturityStage = st.maturityStage)")
    List<Startup> getStartupsByFilters(@Param("status") int status , @Param("mainInStartup") int mainInStartup, @Param("mainInMe") int mainInMe, @Param("maturityStage") int maturityStage);

}
