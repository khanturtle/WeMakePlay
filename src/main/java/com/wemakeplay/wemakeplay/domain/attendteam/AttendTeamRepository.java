package com.wemakeplay.wemakeplay.domain.attendteam;

import com.wemakeplay.wemakeplay.domain.attendboard.Participation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendTeamRepository extends JpaRepository<AttendTeam, Long> {
    List<AttendTeam> findByTeamId(Long teamId);

    List<AttendTeam> findByTeamIdAndParticipation(Long teamId, Participation participation);
}
