package org.rafalzajac.service;


import org.rafalzajac.domain.Player;
import org.rafalzajac.repository.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player addPlayer (Player player) {
        return playerRepository.save(player);
    }

    public Player findPlayerByNumber(int number){
        return playerRepository.findByNumber(number);
    }
}
