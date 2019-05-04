package org.rafalzajac.repository;

import org.rafalzajac.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MatchRepository extends JpaRepository<Match, Long> {

    Optional<Match> findById(Long id);

}
