package org.rafalzajac.repository;

import org.rafalzajac.domain.Round;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoundRepository extends JpaRepository<Round, Long> {
        Round findByRoundNumber(int roundNumber);
}
