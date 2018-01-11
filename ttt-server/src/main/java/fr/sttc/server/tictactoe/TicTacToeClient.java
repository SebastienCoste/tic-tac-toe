package fr.sttc.server.tictactoe;

import fr.sttc.server.tournament.board.Action;
import fr.sttc.server.tournament.client.TournamentClient;

public class TicTacToeClient extends TournamentClient{

    public static final Action unionRepresentative = new TicTacToeAction(0);

    public TicTacToeClient(String url, String gameId, TicTacToeTeam team) {
        super(url, gameId, team);
    }

    @Override
    public Action getActionRepresentative() {
        return unionRepresentative;
    }
}
