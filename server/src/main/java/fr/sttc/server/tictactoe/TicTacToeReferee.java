package fr.sttc.server.tictactoe;

import fr.sttc.server.tournament.validation.GameState;
import fr.sttc.server.tournament.validation.GameStatus;
import fr.sttc.server.tournament.validation.TournamentReferee;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

public class TicTacToeReferee implements TournamentReferee {

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


    public GameStatus evaluateBoard(String[] board) {

        Optional<String> winner = reducePossibilities(board, reduceForWinner(), getWinnerFilter());
        if (winner.isPresent()) {
            return new GameStatus(GameState.ENDED, TicTacToeTeam.CROSS.from(winner.get()));
        } else if (!reducePossibilities(board, reduceForAnyWinnable(), getWinnableFilter()).isPresent()) {
            return new GameStatus(GameState.ENDED, null);
        }
        return new GameStatus(GameState.RUNNING, null);
    }

    private Optional<String> reducePossibilities(String[] board, BinaryOperator<String> reducer, Predicate<String> filter) {
        return ALL_POSSIBILITIES
                .stream()
                .map(lst ->
                        lst.stream().map(idx -> board[idx])
                                .reduce(STARTER_REDUCE,
                                        reducer
                                )
                ).filter(filter)
                .findAny();
    }

    private Predicate<String> getWinnableFilter() {
        return res -> !UNMATCH.equals(res);
    }

    private Predicate<String> getWinnerFilter() {
        return res -> !UNMATCH.equals(res) && !EMPTY.equals(res) && !STARTER_REDUCE.equals(res);
    }

    private BinaryOperator<String> reduceForAnyWinnable() {
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

    private BinaryOperator<String> reduceForWinner() {
        return (l, r) -> {
            if (EMPTY.equals(l) || EMPTY.equals(r)) {
                return STARTER_REDUCE;
            }else if (STARTER_REDUCE.equals(l)) {
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
