package fr.sttc.tournament.client.tournament.tictactoe;

import fr.sttc.tournament.client.tournament.board.Team;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeBoard {

    public List<TicTacToeEvent> events = new ArrayList<>();

    public Boolean addAction(Team team, Integer moveNumber, TicTacToeAction action) {

        return events.add(new TicTacToeEvent(team, moveNumber,action));
    }
}
