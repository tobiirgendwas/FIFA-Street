package com.fifastreet.fifastreet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fifastreet.fifastreet.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    
    long countById(Long locationId);

}
