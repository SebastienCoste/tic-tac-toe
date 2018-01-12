package fr.sttc.server.tournament.validation;

import fr.sttc.server.tournament.board.Team;

import java.util.Objects;

public class GameStatus {

    public final GameState state;
    public final Team winner;

    public GameStatus(GameState state, Team winner) {
        this.state = state;
        this.winner = winner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameStatus that = (GameStatus) o;
        return state == that.state &&
                Objects.equals(winner, that.winner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, winner);
    }
}
