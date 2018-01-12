package fr.sttc.server.api;

import fr.sttc.server.api.tictactoe.TicTacToeRunnableRunnable;
import fr.sttc.server.tournament.board.Action;
import fr.sttc.server.tournament.board.Move;
import fr.sttc.server.tournament.board.ResultTournament;
import fr.sttc.server.tournament.client.TournamentClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class TournamentApiClients {


    private final static Logger logger = LoggerFactory.getLogger(TicTacToeRunnableRunnable.class);

    public static ExecutorService executorService = new ThreadPoolExecutor(1, 8, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public final CompletionService<Action> completionAskingForMoveService;

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

    public Map<Action, Integer> callForVotes(List<TournamentClient> clients, Integer numberOfMove) {
        clients.forEach(client -> completionAskingForMoveService.submit(client::askForMove));

        TournamentClient unionRepresentative = clients.get(0);

        int received = 0;
        Map<Action, Integer> voteByNumber = new HashMap<>();
        while(received < clients.size()) {
            try {
                Future<Action> resultFuture = completionAskingForMoveService.take(); //blocks if none available
                Action move = resultFuture.get();
                voteByNumber.merge(move, 1, (a,b) -> a+b);
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
