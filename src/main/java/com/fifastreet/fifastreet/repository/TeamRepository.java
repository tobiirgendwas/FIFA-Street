package com.fifastreet.fifastreet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fifastreet.fifastreet.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    
    List<Team> findByLocationId(Long locationId);
    long countByLocationId(Long locationId);

}
