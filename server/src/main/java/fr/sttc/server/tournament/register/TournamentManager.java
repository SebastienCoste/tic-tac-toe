package fr.sttc.server.tournament.register;

import fr.sttc.server.tictactoe.Tournament;
import fr.sttc.server.tournament.game.Game;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TournamentManager  {


    Map<String, Tournament> tournaments = new HashMap<>();

    public boolean register (Game game, String gameId, String team, String url){

        return getTournament(game, gameId).register(team, url);
    }

    public boolean start (Game game, String gameId){

        return getTournament(game, gameId).start();
    }

    private synchronized Tournament getTournament(Game game, String gameId) {
        Tournament tournament = tournaments.get(gameId);
        if(tournament == null){
            tournament = TournamentFactory.buildTournament(game, gameId);
            tournaments.put(gameId,tournament);
        }

        return tournament;
    }
}
