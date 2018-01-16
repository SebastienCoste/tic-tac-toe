package fr.sttc.server.tournament.register;

import fr.sttc.server.tictactoe.Tournament;
import fr.sttc.server.tournament.game.Game;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TournamentManager {


    private final Map<String, Tournament> tournaments = new HashMap<>();

    public boolean register(Game game, String gameId, String team, String url) {

        return getTournament(game, gameId).register(team, url);
    }

    public void close(Game game, String gameId) {

        tournaments.remove(getTournamentId(game, gameId));
    }

    public boolean start(Game game, String gameId) {

        return getTournament(game, gameId).start();
    }

    private synchronized Tournament getTournament(Game game, String gameId) {
        return tournaments.computeIfAbsent(getTournamentId(game, gameId),
                i -> TournamentFactory.buildTournament(game, i));
    }

    private String getTournamentId(Game game, String gameId) {
        return gameId;
    }
}
