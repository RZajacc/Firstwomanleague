package org.rafalzajac.service;
import org.rafalzajac.domain.Player;
import org.rafalzajac.domain.PlayerStats;
import org.rafalzajac.repository.PlayerStatsRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerStatsService {

    PlayerStatsRepository playerStatsRepository;

    public PlayerStatsService(PlayerStatsRepository playerStatsRepository) {
        this.playerStatsRepository = playerStatsRepository;
    }

    public PlayerStats savePlayerStats(PlayerStats playerStats) {
        return playerStatsRepository.save(playerStats);
    }
}
