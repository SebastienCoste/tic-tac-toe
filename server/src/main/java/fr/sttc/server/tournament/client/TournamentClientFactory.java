package fr.sttc.server.tournament.client;

import fr.sttc.server.tictactoe.TicTacToeClient;
import fr.sttc.server.tictactoe.TicTacToeTeam;
import fr.sttc.server.tournament.board.Team;
import fr.sttc.server.tournament.game.Game;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TournamentClientFactory {

    public static TournamentClient buildClient(Game game, String url, String gameId, Team team) {

        switch (game) {
            case TICTACTOE:
                return new TicTacToeClient(url, gameId, (TicTacToeTeam) team, game);
            case CONNECT4:
                throw new NotImplementedException();
        }
        return null;
    }

    public static List<TournamentClient> buildClients(Game game, Collection<String> urls, String gameId, Team team) {

        return urls.stream().map(url -> buildClient(game, url, gameId, team)).collect(Collectors.toList());
    }
}
