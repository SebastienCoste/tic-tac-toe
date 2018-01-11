package fr.sttc.ttt.tttserver.tournament.validation;

import fr.sttc.ttt.tttserver.tournament.board.ResultTournament;
import fr.sttc.ttt.tttserver.tournament.board.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;

public class TournamentReferee {

    private static final List<List<Integer>> ALL_POSSIBILITIES = Arrays.asList(
            (List<Integer>) Arrays.asList(0, 1, 2),
            (List<Integer>) Arrays.asList(3, 4, 5),
            (List<Integer>) Arrays.asList(6, 7, 8),
            (List<Integer>) Arrays.asList(0, 3, 6),
            (List<Integer>) Arrays.asList(1, 4, 7),
            (List<Integer>) Arrays.asList(2, 5, 8),
            (List<Integer>) Arrays.asList(0, 4, 8),
            (List<Integer>) Arrays.asList(2, 4, 6)
    );
    public static final String UNMATCH = "!";
    public static final String STARTER_REDUCE = "?";
    public static final String EMPTY = "";


    public static GameStatus evaluateBoard(String[] board) {

        Optional<String> winner = reducePossibilities(board, reduceForWinner());
        if (winner.isPresent()) {
            return new GameStatus(GameState.ENDED, Team.from(winner.get()));
        } else if (!reducePossibilities(board, reduceForAnyWinnable()).isPresent()) {
            return new GameStatus(GameState.ENDED, null);
        }
        return new GameStatus(GameState.RUNNING, null);
    }


    private static Optional<String> reducePossibilities(String[] board, BinaryOperator<String> reducer) {
        return ALL_POSSIBILITIES
                .stream()
                .map(lst ->
                        lst.stream().map(idx -> board[idx])
                                .reduce(STARTER_REDUCE,
                                        reducer
                                )
                ).filter(res -> !UNMATCH.equals(res))
                .findAny();
    }

    private static BinaryOperator<String> reduceForAnyWinnable() {
        return (l, r) -> {
            if (STARTER_REDUCE.equals(l) || EMPTY.equals(l)) {
                return r;
            } else if (STARTER_REDUCE.equals(r) || EMPTY.equals(r)) {
                return l;
            } else if (l.equals(r)) {
                return l;
            } else {
                return UNMATCH;
            }
        };
    }

    private static BinaryOperator<String> reduceForWinner() {
        return (l, r) -> {
            if (EMPTY.equals(l) || EMPTY.equals(r)) {
                return STARTER_REDUCE;
            }
            if (STARTER_REDUCE.equals(l)) {
                return r;
            } else if (STARTER_REDUCE.equals(r)) {
                return l;
            } else if (l.equals(r)) {
                return l;
            } else {
                return UNMATCH;
            }
        };
    }
}
