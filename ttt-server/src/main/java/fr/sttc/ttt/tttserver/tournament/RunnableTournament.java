package fr.sttc.ttt.tttserver.tournament;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RunnableTournament implements Runnable {


    private final static Logger logger = LoggerFactory.getLogger(RunnableTournament.class);

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
    private static final String UNMATCH = "!";
    private static final String STARTER_REDUCE = "?";
    private static final String EMPTY = "";

    private final List<TournamentClient> cross;
    private final List<TournamentClient> round;
    private String[] board;
    private boolean isFinished = false;
    private Team team;
    private String gameId;
    private Integer numberOfMove = 0;

    public static ExecutorService executorService = new ThreadPoolExecutor(1, 8, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());
    CompletionService<Integer> completionAskingForMoveService = new ExecutorCompletionService<>(executorService);

    public RunnableTournament(Set<String> cross, Set<String> round, String gameId) {
        this.cross = cross.stream().map(url -> new TournamentClient(url, gameId)).collect(Collectors.toList());
        this.round = round.stream().map(url -> new TournamentClient(url, gameId)).collect(Collectors.toList());
        team = Team.newTeam();
        board = new String[]{EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY};
        this.gameId = gameId;
    }

    @Override
    public void run() {
        while (!isFinished) {
            Integer position;
            numberOfMove++;
            logger.info(String.format("GAME [%s] MOVE [%s], TEAM [%s]", gameId, numberOfMove.toString(), team.toString()));
            if (team == Team.ROUND) {
                position = callForVote(round);
            } else {
                position = callForVote(cross);
            }
            if (position == null || position < 0 || position > 8) {
                sayWinAndLoose(team.next());
            } else {
                tellMove(team, new Move(gameId, team, position, numberOfMove));
                play(team, position);
                team = team.next();
            }
        }
    }


    private void play(Team team, Integer position) {
        if (!EMPTY.equals(board[position])) {
            sayWinAndLoose(team.next());
        }
        board[position] = team.letter;
        evaluateBoard();
    }

    private void evaluateBoard() {

        Optional<String> winner = reducePossibilities(reduceForWinner());
        if (winner.isPresent()) {
            sayWinAndLoose(Team.from(winner.get()));
        } else if (!reducePossibilities(reduceForAnyWinnable()).isPresent()) {
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

    private void tellMove(Team team, Move move) {

        this.cross.stream().forEach(client -> executorService.submit(() -> client.tellMove(move)));
        this.round.stream().forEach(client -> executorService.submit(() -> client.tellMove(move)));
    }

    private void sayWinAndLoose(Team winningTeam) {


        logger.info(String.format("GAME [%s] MOVE [%s], winning team is %s", gameId, numberOfMove.toString(), winningTeam != null ? winningTeam.toString() : "none"));

        switch (winningTeam) {
            case CROSS:
                this.cross.stream().forEach(client -> executorService.submit(() -> client.tellResult(ResultTournament.WIN)));
                this.round.stream().forEach(client -> executorService.submit(() -> client.tellResult(ResultTournament.LOOSE)));
                return;
            case ROUND:
                this.round.stream().forEach(client -> executorService.submit(() -> client.tellResult(ResultTournament.WIN)));
                this.cross.stream().forEach(client -> executorService.submit(() -> client.tellResult(ResultTournament.LOOSE)));
                return;
            default:
                this.round.stream().forEach(client -> executorService.submit(() -> client.tellResult(ResultTournament.TIE)));
                this.cross.stream().forEach(client -> executorService.submit(() -> client.tellResult(ResultTournament.TIE)));
        }
        isFinished = true;
    }

    private Integer callForVote(List<TournamentClient> clients) {


        clients.stream().map(client -> completionAskingForMoveService.submit(() -> client.askForMove())).collect(Collectors.toList());

        int received = 0;
        Map<Integer, Integer> voteByNumber = new HashMap<>();
        IntStream.rangeClosed(0, 8).forEach(i -> voteByNumber.put(i, 0));
        while(received < clients.size()) {
            try {
                Future<Integer> resultFuture = completionAskingForMoveService.take(); //blocks if none available
                Integer move = resultFuture.get();
                voteByNumber.put(move, voteByNumber.get(move) +1);
                logger.info(String.format("GAME [%s] MOVE [%s], TEAM [%s] vote for %s", gameId, numberOfMove.toString(), team.toString(), move.toString()));
            }
            catch(Exception e) {
                logger.error(e.getMessage());
            } finally {
                received ++;
            }
        }

        Map.Entry<Integer, Integer> entry = voteByNumber.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get();

        return entry.getValue() == 0 ? null : entry.getKey();
    }
}