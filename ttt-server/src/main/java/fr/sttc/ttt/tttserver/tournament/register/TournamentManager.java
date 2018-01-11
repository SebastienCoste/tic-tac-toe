package fr.sttc.ttt.tttserver.tournament.register;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TournamentManager {


    Map<String, Tournament> tournaments = new HashMap<>();

    public boolean register (String gameId, String team, String url){

        return getTournament(gameId).register(team, url);
    }

    public boolean start (String gameId){

        return getTournament(gameId).start();
    }

    private synchronized Tournament getTournament(String gameId) {
        Tournament tournament = tournaments.get(gameId);
        if(tournament == null){
            tournament = new Tournament(gameId);
            tournaments.put(gameId,tournament);
        }

        return tournament;
    }
}
