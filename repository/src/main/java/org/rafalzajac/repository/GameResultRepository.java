package org.rafalzajac.repository;

import org.rafalzajac.domain.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameResultRepository extends JpaRepository<GameResult, Long> {
}
