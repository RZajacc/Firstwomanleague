package org.rafalzajac.repository;

import org.rafalzajac.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface MatchRepository extends JpaRepository<Game, Long> {

    Optional<Game> findById(Long id);
    void deleteById(Long id);

}
