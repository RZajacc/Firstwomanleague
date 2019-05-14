package org.rafalzajac.repository;

import org.rafalzajac.domain.PlayerStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerStatsRepository extends JpaRepository<PlayerStats, Long> {

}
