package org.rafalzajac.repository;

import org.rafalzajac.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findById(Long id);
    Team findByTeamTag(String teamTag);
    Team findByTeamName(String teamName);
}
