package fr.sttc.server.tournament.validation;

import fr.sttc.server.tournament.board.Team;

public class GameStatus {

    public final GameState state;
    public final Team winner;

    public GameStatus(GameState state, Team winner) {
        this.state = state;
        this.winner = winner;
    }
}
