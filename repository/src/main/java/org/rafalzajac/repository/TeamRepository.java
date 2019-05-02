package org.rafalzajac.repository;

import org.rafalzajac.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
