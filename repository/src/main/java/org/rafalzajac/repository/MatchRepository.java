package org.rafalzajac.repository;

import org.rafalzajac.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MatchRepository extends JpaRepository<Match, Long> {
}
