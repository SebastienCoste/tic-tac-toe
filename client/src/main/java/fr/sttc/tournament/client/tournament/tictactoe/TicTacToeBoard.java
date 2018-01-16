package fr.sttc.tournament.client.tournament.tictactoe;

import fr.sttc.tournament.client.tournament.board.Team;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeBoard {

    private static final String EMPTY = "_";
    public List<TicTacToeEvent> events = new ArrayList<>();

    public Boolean addAction(Team team, Integer moveNumber, TicTacToeAction action) {

        return events.add(new TicTacToeEvent(team, moveNumber,action));
    }

    public String getBoard() {
        String[] board = new String[]{EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY};
        events.forEach(e -> board[e.action.position] = e.team.getLetter());

        return  "\n" + board[0] +
                board[1] +
                board[2] + "\n" +
                board[3] +
                board[4] +
                board[5] + "\n" +
                board[6] +
                board[7] +
                board[8];

    }
}
