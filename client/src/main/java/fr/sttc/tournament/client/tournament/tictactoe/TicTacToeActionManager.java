package fr.sttc.tournament.client.tournament.tictactoe;

import fr.sttc.tournament.client.tournament.board.Action;
import fr.sttc.tournament.client.tournament.board.Team;
import fr.sttc.tournament.client.tournament.game.ActionManager;
import fr.sttc.tournament.client.tournament.game.ResultTournament;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Service
public class TicTacToeActionManager implements ActionManager {

    Map<String, TicTacToeBoard> allBoards = new HashMap<>();


    @Override
    public Action askForMove(String gameId) {
        TicTacToeBoard ticTacToeBoard = allBoards.get(gameId);
        return new TicTacToeAction(
                IntStream.range(0, 8)
                        .filter(a -> ticTacToeBoard.events
                                .stream()
                                .map(e -> e.action.position)
                                .noneMatch(p -> p.equals(a))
                        )
                        .findAny()
                        .orElse(0));
    }

    @Override
    public Boolean tellTheMove(String gameId, Team team, Integer moveNumber, Action action) {
        TicTacToeBoard ticTacToeBoard = allBoards.get(gameId);
        return ticTacToeBoard.addAction(team, moveNumber, (TicTacToeAction) action);
    }

    @Override
    public Boolean tellTheResult(String gameId, ResultTournament result) {

        return allBoards.remove(gameId) != null;
    }
}
