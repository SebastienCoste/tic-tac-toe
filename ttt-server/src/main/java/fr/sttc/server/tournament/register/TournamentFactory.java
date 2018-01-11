package fr.sttc.server.tournament.register;

import fr.sttc.server.tictactoe.Tournament;
import fr.sttc.server.tournament.client.TournamentClient;
import fr.sttc.server.tournament.game.Game;

public class TournamentFactory {


    public static Tournament buildTournament(Game game, String gameId ){
        switch (game){
            case TICTACTOE:
                return new Tournament(gameId);

            case CONNECT4:
                return new Tournament(gameId);
        }

        return null;
    }

}
