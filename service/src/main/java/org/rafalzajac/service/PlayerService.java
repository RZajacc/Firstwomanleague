package org.rafalzajac.service;


import org.rafalzajac.domain.Player;
import org.rafalzajac.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void addPlayer (Player player) {
        playerRepository.save(player);
    }

    public List<Player> findAllPlayers() {
        return playerRepository.findAll();
    }

    public Optional<Player> findPlayerById(Long id) {
        return playerRepository.findById(id);
    }

    public void deletePlayerById(Long id) {
        playerRepository.deleteById(id);
    }

}
