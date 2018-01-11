package fr.sttc.ttt.tttserver.tournament.validation;

import fr.sttc.ttt.tttserver.tournament.board.Team;

public class GameStatus {

    public final GameState state;
    public final Team winner;

    public GameStatus(GameState state, Team winner) {
        this.state = state;
        this.winner = winner;
    }
}
