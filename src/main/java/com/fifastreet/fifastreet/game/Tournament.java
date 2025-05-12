package com.fifastreet.fifastreet.game;

import java.util.List;

import com.fifastreet.fifastreet.model.Team;

public class Tournament {
    
    private TournamentType type;
    private List<Match> matches;
    private List<Team> participants;
    private String status; // e.g. "RUNNING", "FINISHED"
}
