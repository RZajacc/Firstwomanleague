package org.rafalzajac.repository;

import org.rafalzajac.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;


public interface GameRepository extends JpaRepository<Game, Long> {

    Optional<Game> findById(Long id);
    void deleteById(Long id);

}
