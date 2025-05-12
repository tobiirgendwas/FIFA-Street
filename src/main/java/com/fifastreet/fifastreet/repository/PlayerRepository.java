package com.fifastreet.fifastreet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fifastreet.fifastreet.model.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByTeamId(Long teamId);
    long countByTeamId(Long teamId);
    long countByTeamIdAndPosition(Long teamId, String position);

}
