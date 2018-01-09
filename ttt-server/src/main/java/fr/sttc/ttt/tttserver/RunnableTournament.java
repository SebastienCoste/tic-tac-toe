package fr.sttc.ttt.tttserver;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class RunnableTournament  implements Runnable {

    private static final List<List<Integer>> ALL_POSSIBILITIES = Arrays.asList(
            (List<Integer>) Arrays.asList(0,1,2),
            (List<Integer>) Arrays.asList(3,4,5),
            (List<Integer>) Arrays.asList(6,7,8),
            (List<Integer>) Arrays.asList(0,3,6),
            (List<Integer>) Arrays.asList(1,4,7),
            (List<Integer>) Arrays.asList(2,5,8),
            (List<Integer>) Arrays.asList(0,4,8),
            (List<Integer>) Arrays.asList(2,4,6)
    );
    private static final String UNMATCH = "!";
    private static final String STARTER_REDUCE = "?";
    private static final String EMPTY = "";

    private final List<TournamentClient> cross;
    private final List<TournamentClient> round;
    private String[] board;
    private boolean isFinished = false;
    private Team team;

    public RunnableTournament(Set<String> cross, Set<String> round, String gameId) {
        this.cross = cross.stream().map(url -> new TournamentClient(url, gameId)).collect(Collectors.toList());
        this.round = round.stream().map(url -> new TournamentClient(url, gameId)).collect(Collectors.toList());
        team = Team.newTeam();
        board = new String[] {EMPTY,EMPTY,EMPTY, EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY};
    }

    @Override
    public void run() {
        while(!isFinished){
            Integer position;
            if(team == Team.ROUND){
                position = callForVote(round);
            } else {
                position = callForVote(cross);
            }
            if (position == null || position < 0 || position > 8){
                sayWinAndLoose(team.next());
            } else {
                tellMove(team.next(), position);
                play(team, position);
                team = team.next();
            }
        }
    }


    private void play(Team team, Integer position) {
        if(!EMPTY.equals(board[position])){
            sayWinAndLoose(team.next());
        }
        board[position] = team.letter;
        evaluateBoard();
    }

    private void evaluateBoard() {
        
        Optional<String> winner = reducePossibilities(reduceForWinner());
        if(winner.isPresent()){
            sayWinAndLoose(Team.from(winner.get()));
        } else if(!reducePossibilities(reduceForAnyWinnable()).isPresent()){
            sayWinAndLoose(null);
        }
    }



    private Optional<String> reducePossibilities(BinaryOperator<String> reducer) {
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

    private BinaryOperator<String> reduceForAnyWinnable() {
        return (l, r) -> {
            if (STARTER_REDUCE.equals(l) || EMPTY.equals(l)) {
                return r;
            } else if (STARTER_REDUCE.equals(r)  || EMPTY.equals(r)) {
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

    private void tellMove(Team next, Integer position) {

    }

    private void sayWinAndLoose(Team next) {

        isFinished = true;
    }

    private Integer callForVote(List<TournamentClient> round) {

        return null;
    }
}
