package fr.sttc.server.tictactoe;

import fr.sttc.server.api.TournamentApiClients;
import fr.sttc.server.tournament.board.Action;
import fr.sttc.server.tournament.board.Move;
import fr.sttc.server.tournament.board.TournamentBoard;
import fr.sttc.server.tournament.client.TournamentClient;
import fr.sttc.server.tournament.validation.GameState;
import fr.sttc.server.tournament.validation.GameStatus;
import fr.sttc.server.tournament.validation.TournamentReferee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TicTacToeBoard implements TournamentBoard {


    private final static Logger logger = LoggerFactory.getLogger(TicTacToeBoard.class);

    private final TournamentApiClients tournamentApiClients;
    private final TournamentReferee referee = new TicTacToeReferee();

    private final List<TournamentClient> cross;
    private final List<TournamentClient> round;
    private final String[] board;
    public boolean isFinished = false;
    private TicTacToeTeam team;
    public final String gameId;
    private Integer numberOfMove = 0;

    public TicTacToeBoard(List<TournamentClient> cross, List<TournamentClient> round, String gameId) {
        this.cross = cross;
        this.round = round;
        team = TicTacToeTeam.CROSS.newTeam();
        board = new String[]{TicTacToeReferee.EMPTY, TicTacToeReferee.EMPTY, TicTacToeReferee.EMPTY, TicTacToeReferee.EMPTY, TicTacToeReferee.EMPTY, TicTacToeReferee.EMPTY, TicTacToeReferee.EMPTY, TicTacToeReferee.EMPTY, TicTacToeReferee.EMPTY};
        this.gameId = gameId;

        tournamentApiClients = new TournamentApiClients();
    }

    @Override
    public void runNextMove() {
        if (isFinished) {
            logger.error("game is finished, why keep asking the board ?");
        } else {
            numberOfMove++;
            logger.info(String.format("GAME [%s] MOVE [%s], TEAM [%s]", gameId, numberOfMove.toString(), team.toString()));
            Integer position = requestVotesToPlayers(team == TicTacToeTeam.ROUND ? round : cross).value();
            tellMove(new Move(gameId, team, new TicTacToeAction(position), numberOfMove));
            if (position == null || position < 0 || position > 8) {
                sayWinAndLoose(team.next());
            } else {
                play(team, position);
                team = team.next();
            }
        }
    }

    private void play(TicTacToeTeam team, Integer position) {
        if (!TicTacToeReferee.EMPTY.equals(board[position])) {
            sayWinAndLoose(team.next());
        }
        board[position] = team.letter;
        GameStatus status = referee.evaluateBoard(board);
        if (status.state == GameState.ENDED) {
            sayWinAndLoose((TicTacToeTeam) status.winner);
        }
    }


    private void tellMove(Move move) {
        tournamentApiClients.tellThemThisMove(this.cross, move);
        tournamentApiClients.tellThemThisMove(this.round, move);
    }

    private void sayWinAndLoose(TicTacToeTeam winningTeam) {


        logger.info(String.format("GAME [%s] MOVE [%s], winning team is %s", gameId, numberOfMove.toString(), winningTeam != null ? winningTeam.toString() : "none"));

        if (winningTeam == null) {
            tournamentApiClients.tellThemItsATie(this.cross);
            tournamentApiClients.tellThemItsATie(this.round);
        } else {

            switch (winningTeam) {
                case CROSS:
                    tournamentApiClients.tellThemTheyWon(this.cross);
                    tournamentApiClients.tellThemTheyLost(this.round);
                    break;
                case ROUND:
                    tournamentApiClients.tellThemTheyWon(this.round);
                    tournamentApiClients.tellThemTheyLost(this.cross);
                    break;
            }
        }
        isFinished = true;
    }

    private TicTacToeAction requestVotesToPlayers(List<TournamentClient> clients) {


        Map<Action, Integer> voteByNumber = tournamentApiClients.callForVotes(clients, numberOfMove);

        Map.Entry<Action, Integer> entry = voteByNumber.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).orElse(null);

        return entry == null || entry.getValue() == 0 ? TicTacToeAction.EMPTY : (TicTacToeAction) entry.getKey();
    }

    @Override
    public String toString() {
        return board[0] +
                board[1] +
                board[2] + "\n" +
                board[3] +
                board[4] +
                board[5] + "\n" +
                board[6] +
                board[7] +
                board[8];
    }

}
