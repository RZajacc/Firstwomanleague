package org.rafalzajac.repository;

import org.rafalzajac.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findById(Long id);
    void deleteById(Long id);
}
