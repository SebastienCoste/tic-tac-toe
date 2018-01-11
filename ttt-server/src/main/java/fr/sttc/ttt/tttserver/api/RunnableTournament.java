package fr.sttc.ttt.tttserver.api;

import fr.sttc.ttt.tttserver.tournament.board.Team;
import fr.sttc.ttt.tttserver.tournament.board.TournamentBoard;
import fr.sttc.ttt.tttserver.tournament.client.TournamentClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RunnableTournament implements Runnable {


    private final static Logger logger = LoggerFactory.getLogger(RunnableTournament.class);


    public TournamentBoard tournamentBoard;

    public RunnableTournament(Set<String> cross, Set<String> round, String gameId) {
        tournamentBoard = new TournamentBoard(
                TournamentClientFactory.buildClients(cross, gameId, Team.CROSS),
                TournamentClientFactory.buildClients(round, gameId, Team.ROUND),
                gameId
        );
    }

    @Override
    public void run() {
        while (!tournamentBoard.isFinished) {
            tournamentBoard.runNextMove();
        }
    }



}