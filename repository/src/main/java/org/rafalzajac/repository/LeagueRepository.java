package org.rafalzajac.repository;

import org.rafalzajac.domain.League;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeagueRepository extends JpaRepository<League, Long> {

    Optional<League> findById(Long id);
}
