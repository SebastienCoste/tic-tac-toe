package fr.sttc.tournament.client.tournament.tictactoe;

import fr.sttc.tournament.client.tournament.board.Team;

class TicTacToeEvent {
    public final Team team;
    private final Integer moveNumber;
    public final TicTacToeAction action;


    public TicTacToeEvent(Team team, Integer moveNumber, TicTacToeAction action) {
        this.team = team;
        this.moveNumber = moveNumber;
        this.action = action;
    }

    public String getBoardValue() {
        return team == null ? " " : team.getLetter();
    }

    @Override
    public String toString() {
        return "TicTacToeEvent{" +
                "team=" + team +
                ", moveNumber=" + moveNumber +
                ", action=" + action +
                '}';
    }
}
