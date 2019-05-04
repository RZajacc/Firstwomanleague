package org.rafalzajac.repository;

import org.rafalzajac.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TeamRepository extends JpaRepository<Team, Long> {

}
