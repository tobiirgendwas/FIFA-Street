package com.fifastreet.fifastreet.game;

import java.util.Random;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fifastreet.fifastreet.model.Player;
import com.fifastreet.fifastreet.model.Team;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MatchService {
    private final Random random = new Random();

    /**
     * Simuliert ein Spiel basierend auf der Teamstärke gemäß definierter Regel.
     * Gewinner wird nach Wahrscheinlichkeiten basierend auf Stärke berechnet.
     */
    public Team playMatch(Team teamA, Team teamB) {
        double strengthA = calculateTeamStrength(teamA);
        double strengthB = calculateTeamStrength(teamB);
        double total = strengthA + strengthB;

        double chanceA = strengthA / total;
        double randomValue = random.nextDouble();

        Team winner = (randomValue < chanceA) ? teamA : teamB;

        log.info("Spiel: {} ({}) vs {} ({}), ChanceA: {}, RNG: {}, Gewinner: {}",
                teamA.getName(), strengthA,
                teamB.getName(), strengthB,
                String.format("%.2f", chanceA),
                String.format("%.2f", randomValue),
                winner.getName());

        return winner;
    }

    /**
     * Berechnet die Teamstärke:
     * (Summe der 3 Feldspieler + Torwart*2) / 5
     */
    private double calculateTeamStrength(Team team) {
        List<Player> players = team.getPlayers();
        if (players == null || players.size() < 4) return 0;

        double fieldPlayersStrength = players.stream()
                .filter(p -> !p.getPosition().equalsIgnoreCase("Torwart"))
                .limit(3)
                .mapToDouble(Player::getStrength)
                .sum();

        double goalkeeperStrength = players.stream()
                .filter(p -> p.getPosition().equalsIgnoreCase("Torwart"))
                .findFirst()
                .map(Player::getStrength)
                .orElse(0.0) * 2;

        return (fieldPlayersStrength + goalkeeperStrength) / 5.0;
    }
}
