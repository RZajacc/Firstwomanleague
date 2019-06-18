package org.rafalzajac.repository;

import org.rafalzajac.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MatchRepository extends JpaRepository<Game, Long> {

    Optional<Game> findById(Long id);

}
