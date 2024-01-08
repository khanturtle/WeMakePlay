package com.wemakeplay.wemakeplay.domain.team.repository;

import com.wemakeplay.wemakeplay.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
