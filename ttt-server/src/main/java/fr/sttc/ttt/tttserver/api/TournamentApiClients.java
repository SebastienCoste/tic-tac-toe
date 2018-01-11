package fr.sttc.ttt.tttserver.api;

import fr.sttc.ttt.tttserver.tournament.board.Move;
import fr.sttc.ttt.tttserver.tournament.board.ResultTournament;
import fr.sttc.ttt.tttserver.tournament.client.TournamentClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TournamentApiClients {


    private final static Logger logger = LoggerFactory.getLogger(RunnableTournament.class);

    public static ExecutorService executorService = new ThreadPoolExecutor(1, 8, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public final CompletionService<Integer> completionAskingForMoveService;

    public TournamentApiClients() {
        completionAskingForMoveService = new ExecutorCompletionService<>(executorService);
    }

    public void tellThemThisMove(List<TournamentClient> clients, Move move){

        clients.forEach(client -> executorService.submit(() -> client.tellMove(move)));
    }

    public void tellThemTheyWon(List<TournamentClient> clients){
        tellThemThisResult(clients, ResultTournament.WIN);
    }

    public void tellThemTheyLost(List<TournamentClient> clients){
        tellThemThisResult(clients, ResultTournament.LOOSE);
    }

    public void tellThemItsATie(List<TournamentClient> clients){
        tellThemThisResult(clients, ResultTournament.TIE);
    }

    private void tellThemThisResult(List<TournamentClient> clients, ResultTournament result){
        clients.forEach(client -> executorService.submit(() -> client.tellResult(result)));
    }

    public Map<Integer, Integer> callForVotes(List<TournamentClient> clients, Integer numberOfMove) {
        clients.stream().forEach(client -> completionAskingForMoveService.submit(client::askForMove));

        TournamentClient unionRepresentative = clients.get(0);

        int received = 0;
        Map<Integer, Integer> voteByNumber = new HashMap<>();
        IntStream.rangeClosed(0, 8).forEach(i -> voteByNumber.put(i, 0));
        while(received < clients.size()) {
            try {
                Future<Integer> resultFuture = completionAskingForMoveService.take(); //blocks if none available
                Integer move = resultFuture.get();
                voteByNumber.put(move, voteByNumber.get(move) +1);
                logger.info(String.format("GAME [%s] MOVE [%s], TEAM [%s] vote for %s", unionRepresentative.gameId, numberOfMove.toString(), unionRepresentative.team.toString(), move.toString()));
            }
            catch(Exception e) {
                logger.error(e.getMessage());
            } finally {
                received ++;
            }
        }
        return voteByNumber;
    }
}
