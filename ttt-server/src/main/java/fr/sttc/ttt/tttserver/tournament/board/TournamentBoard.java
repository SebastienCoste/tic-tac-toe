package fr.sttc.ttt.tttserver.tournament.board;

import fr.sttc.ttt.tttserver.api.TournamentApiClients;
import fr.sttc.ttt.tttserver.tournament.client.TournamentClient;
import fr.sttc.ttt.tttserver.tournament.validation.GameState;
import fr.sttc.ttt.tttserver.tournament.validation.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static fr.sttc.ttt.tttserver.tournament.validation.TournamentReferee.EMPTY;
import static fr.sttc.ttt.tttserver.tournament.validation.TournamentReferee.evaluateBoard;

public class TournamentBoard {


    private final static Logger logger = LoggerFactory.getLogger(TournamentBoard.class);

    public final TournamentApiClients tournamentApiClients;

    private final List<TournamentClient> cross;
    private final List<TournamentClient> round;
    private String[] board;
    public boolean isFinished = false;
    private Team team;
    private String gameId;
    private Integer numberOfMove = 0;

    public TournamentBoard(List<TournamentClient> cross, List<TournamentClient> round, String gameId) {
        this.cross = cross;
        this.round = round;
        team = Team.newTeam();
        board = new String[]{EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY};
        this.gameId = gameId;

        tournamentApiClients = new TournamentApiClients();
    }

    public void runNextMove() {
        Integer position;
        numberOfMove++;
        logger.info(String.format("GAME [%s] MOVE [%s], TEAM [%s]", gameId, numberOfMove.toString(), team.toString()));
        if (team == Team.ROUND) {
            position = requestVotesToPlayers(round);
        } else {
            position = requestVotesToPlayers(cross);
        }
        if (position == null || position < 0 || position > 8) {
            sayWinAndLoose(team.next());
        } else {
            tellMove(new Move(gameId, team, position, numberOfMove));
            play(team, position);
            team = team.next();
        }
    }

    private void play(Team team, Integer position) {
        if (!EMPTY.equals(board[position])) {
            sayWinAndLoose(team.next());
        }
        board[position] = team.letter;
        GameStatus status = evaluateBoard(board);
        if(status.state == GameState.ENDED){
            sayWinAndLoose(status.winner);
        }
    }


    private void tellMove(Move move) {
        tournamentApiClients.tellThemThisMove(this.cross, move);
        tournamentApiClients.tellThemThisMove(this.round, move);
    }

    private void sayWinAndLoose(Team winningTeam) {


        logger.info(String.format("GAME [%s] MOVE [%s], winning team is %s", gameId, numberOfMove.toString(), winningTeam != null ? winningTeam.toString() : "none"));

        if(winningTeam == null){
            tournamentApiClients.tellThemItsATie(this.cross);
            tournamentApiClients.tellThemItsATie(this.round);
        } else {

            switch (winningTeam) {
                case CROSS:
                    tournamentApiClients.tellThemTheyWon(this.cross);
                    tournamentApiClients.tellThemTheyLost(this.round);
                    return;
                case ROUND:
                    tournamentApiClients.tellThemTheyWon(this.round);
                    tournamentApiClients.tellThemTheyLost(this.cross);
                    return;
            }
        }
        isFinished = true;
    }

    private Integer requestVotesToPlayers(List<TournamentClient> clients) {


        Map<Integer, Integer> voteByNumber = tournamentApiClients.callForVotes(clients, numberOfMove);

        Map.Entry<Integer, Integer> entry = voteByNumber.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).orElse(null);

        return entry == null || entry.getValue() == 0 ? null : entry.getKey();
    }
}
