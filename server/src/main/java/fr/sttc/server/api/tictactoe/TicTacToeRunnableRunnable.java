package fr.sttc.server.api.tictactoe;

import fr.sttc.server.StaticApplicationContext;
import fr.sttc.server.api.TournamentRunnable;
import fr.sttc.server.tictactoe.TicTacToeBoard;
import fr.sttc.server.tictactoe.TicTacToeTeam;
import fr.sttc.server.tournament.client.TournamentClientFactory;
import fr.sttc.server.tournament.game.Game;
import fr.sttc.server.tournament.register.TournamentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class TicTacToeRunnableRunnable implements TournamentRunnable {


    private final static Logger logger = LoggerFactory.getLogger(TicTacToeRunnableRunnable.class);


    private final TicTacToeBoard tournamentBoard;

    public TicTacToeRunnableRunnable(Set<String> cross, Set<String> round, String gameId) {
        tournamentBoard = new TicTacToeBoard(
                TournamentClientFactory.buildClients(Game.TICTACTOE, cross, gameId, TicTacToeTeam.CROSS),
                TournamentClientFactory.buildClients(Game.TICTACTOE, round, gameId, TicTacToeTeam.ROUND),
                gameId
        );
    }

    @Override
    public void run() {
        while (!tournamentBoard.isFinished) {
            tournamentBoard.runNextMove();
        }
        StaticApplicationContext.getContext().getBean(TournamentManager.class).close(Game.TICTACTOE, tournamentBoard.gameId);
    }


}