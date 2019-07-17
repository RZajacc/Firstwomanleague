package org.rafalzajac.repository;

import org.rafalzajac.domain.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchResultRepository extends JpaRepository<GameResult, Long> {
}
