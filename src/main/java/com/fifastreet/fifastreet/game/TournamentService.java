package com.fifastreet.fifastreet.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fifastreet.fifastreet.model.Team;
import com.fifastreet.fifastreet.repository.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TournamentService {
    
    private final TeamRepository teamRepository;
    private final MatchService matchService;

    public List<Team> startTournament(TournamentType type) {
        return switch (type) {
            case ABI_CHAMPIONS -> runAbiChampionsLeague();
            case ELITE -> runEliteTournament();
            case WORLD_FINAL -> runWorldFinalTournament();
        };
    }

    // ========================================
    // 1. ABI-Champions League (Gruppenphase)
    // ========================================
    private List<Team> runAbiChampionsLeague() {
        List<Team> allTeams = teamRepository.findAll();

        // Gruppierung nach Region/Spielort
        Map<String, List<Team>> groupedByRegion = allTeams.stream()
                .collect(Collectors.groupingBy(Team::getRegion));

        List<Team> qualifiedTeams = new ArrayList<>();

        for (Map.Entry<String, List<Team>> entry : groupedByRegion.entrySet()) {
            List<Team> group = entry.getValue();
            if (group.size() != 4) continue;

            Map<Team, Integer> pointsMap = new HashMap<>();

            // Jeder gegen jeden in der Gruppe
            for (int i = 0; i < group.size(); i++) {
                for (int j = i + 1; j < group.size(); j++) {
                    Team team1 = group.get(i);
                    Team team2 = group.get(j);
                    Team winner = matchService.playMatch(team1, team2);
                    
                    pointsMap.put(winner, pointsMap.getOrDefault(winner, 0) + 3);
                }
            }

            // Teams nach Punkten sortieren
            List<Team> sorted = group.stream()
                    .sorted(Comparator.comparingInt(t -> -pointsMap.getOrDefault(t, 0)))
                    .toList();

            if (sorted.size() >= 2) {
                qualifiedTeams.add(sorted.get(0)); // Sieger
                qualifiedTeams.add(sorted.get(1)); // Zweitplatzierter
            }
        }

        return qualifiedTeams;
    }

    // ========================================
    // 2. Elite Turnier (KO mit 16 Teams)
    // ========================================
    private List<Team> runEliteTournament() {
        List<Team> teams = runAbiChampionsLeague(); // oder separat speichern
        return runKnockout(teams);
    }

    // ========================================
    // 3. World Final (KO mit allen 32 Teams)
    // ========================================
    private List<Team> runWorldFinalTournament() {
        List<Team> teams = teamRepository.findAll();
        return runKnockout(teams);
    }

    // ========================================
    // Allgemeiner KO-Modus
    // ========================================
    private List<Team> runKnockout(List<Team> teams) {
        if (teams.size() < 2) return teams;

        List<Team> currentRound = new ArrayList<>(teams);

        while (currentRound.size() > 1) {
            Collections.shuffle(currentRound);
            List<Team> nextRound = new ArrayList<>();

            for (int i = 0; i < currentRound.size(); i += 2) {
                if (i + 1 < currentRound.size()) {
                    Team winner = matchService.playMatch(currentRound.get(i), currentRound.get(i + 1));
                    nextRound.add(winner);
                }
            }
            currentRound = nextRound;
        }

        return currentRound; // Gewinnerteam
    }
}
