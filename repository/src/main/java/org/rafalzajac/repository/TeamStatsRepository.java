package org.rafalzajac.repository;

import org.rafalzajac.domain.TeamStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamStatsRepository extends JpaRepository<TeamStats, Long> {
}
