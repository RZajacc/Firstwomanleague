package org.rafalzajac.repository;

import org.rafalzajac.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByNumber(int number);
}
