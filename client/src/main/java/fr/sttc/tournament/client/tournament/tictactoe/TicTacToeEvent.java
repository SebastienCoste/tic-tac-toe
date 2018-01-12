package fr.sttc.tournament.client.tournament.tictactoe;

import fr.sttc.tournament.client.tournament.board.Team;

public class TicTacToeEvent {
    public Team team;
    public Integer moveNumber;
    public TicTacToeAction action;


    public TicTacToeEvent(Team team, Integer moveNumber, TicTacToeAction action) {
        this.team = team;
        this.moveNumber = moveNumber;
        this.action = action;
    }
}