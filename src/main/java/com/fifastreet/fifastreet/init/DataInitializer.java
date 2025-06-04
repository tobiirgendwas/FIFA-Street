package com.fifastreet.fifastreet.init;

import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fifastreet.fifastreet.repository.LocationRepository;
import com.fifastreet.fifastreet.repository.PlayerRepository;
import com.fifastreet.fifastreet.repository.TeamRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner{

    private final LocationRepository locationRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        if (locationRepository.count() == 0) {
            initData();
        }
    }

    private void initData() {
        // Initialize locations, teams, and players here
        for (int i = 0; i < 10; i++) {
            String locationName = "Location " + (i + 1);
            var location = new com.fifastreet.fifastreet.model.Location();
            location.setName(locationName);
            locationRepository.save(location);

            for (int j = 0; j < 5; j++) {
                String teamName = "Team " + (j + 1) + " in " + locationName;
                var team = new com.fifastreet.fifastreet.model.Team();
                team.setName(teamName);
                team.setRegion(locationName);
                team.setLocation(location);
                teamRepository.save(team);

                for (int k = 0; k < 11; k++) {
                    String playerName = "Player " + (k + 1) + " in " + teamName;
                    var player = new com.fifastreet.fifastreet.model.Player();
                    player.setName(playerName);
                    player.setPosition(randomPosition());
                    player.setStrength(randomStrength());
                    player.setTeam(team);
                    playerRepository.save(player);
                }
            }
        }
    }

    private String randomPosition() {
        String[] positions = { "Torwart", "Verteidiger", "Mittelfeld", "Stürmer" };
        return positions[random.nextInt(positions.length)];
    }

    private double randomStrength() {
        return 50 + random.nextInt(51); // 50 - 100
    }
}
