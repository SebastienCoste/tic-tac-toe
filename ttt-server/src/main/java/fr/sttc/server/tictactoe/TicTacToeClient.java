package fr.sttc.server.tictactoe;

import fr.sttc.server.tournament.board.Action;
import fr.sttc.server.tournament.client.TournamentClient;
import fr.sttc.server.tournament.game.Game;

public class TicTacToeClient extends TournamentClient{

    public static final Action unionRepresentative = new TicTacToeAction(0);

    public TicTacToeClient(String url, String gameId, TicTacToeTeam team, Game game) {
        super(url, gameId, team, game);
    }

    @Override
    public Action getActionRepresentative() {
        return unionRepresentative;
    }
}
