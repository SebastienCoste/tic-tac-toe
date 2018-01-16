package fr.sttc.server.tournament.register;

import fr.sttc.server.tictactoe.Tournament;
import fr.sttc.server.tournament.client.TournamentClient;
import fr.sttc.server.tournament.game.Game;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class TournamentFactory {


    public static Tournament buildTournament(Game game, String tournamentId ){
        switch (game){
            case TICTACTOE:
                return new Tournament(tournamentId);

            case CONNECT4:
                throw new NotImplementedException();
        }

        return null;
    }

}
