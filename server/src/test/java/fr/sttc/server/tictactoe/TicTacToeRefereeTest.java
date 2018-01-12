package fr.sttc.server.tictactoe;

import fr.sttc.server.tournament.validation.GameState;
import fr.sttc.server.tournament.validation.GameStatus;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TicTacToeRefereeTest {

    private final TicTacToeReferee ticTacToeReferee = new TicTacToeReferee();

    @Test
    public void evaluateBoard() throws Exception {
        assertThat(ticTacToeReferee.evaluateBoard(new String[] {"", "", "", "", "", "", "", "", ""})).isEqualTo(new GameStatus(GameState.RUNNING, null));
        assertThat(ticTacToeReferee.evaluateBoard(new String[] {"", "", "C", "", "", "", "", "", ""})).isEqualTo(new GameStatus(GameState.RUNNING, null));
        assertThat(ticTacToeReferee.evaluateBoard(new String[] {"", "C", "", "", "", "", "", "", ""})).isEqualTo(new GameStatus(GameState.RUNNING, null));
        assertThat(ticTacToeReferee.evaluateBoard(new String[] {"C", "", "", "", "", "", "", "", ""})).isEqualTo(new GameStatus(GameState.RUNNING, null));
        assertThat(ticTacToeReferee.evaluateBoard(new String[] {"C", "C", "", "", "", "", "", "", ""})).isEqualTo(new GameStatus(GameState.RUNNING, null));
        assertThat(ticTacToeReferee.evaluateBoard(new String[] {"C", "", "C", "", "", "", "", "", ""})).isEqualTo(new GameStatus(GameState.RUNNING, null));
        assertThat(ticTacToeReferee.evaluateBoard(new String[] {"", "C", "C", "", "", "", "", "", ""})).isEqualTo(new GameStatus(GameState.RUNNING, null));
        assertThat(ticTacToeReferee.evaluateBoard(new String[] {"C", "C", "C", "", "", "", "", "", ""})).isEqualTo(new GameStatus(GameState.ENDED, TicTacToeTeam.CROSS));
        assertThat(ticTacToeReferee.evaluateBoard(new String[] {"", "", "", "R", "R", "R", "", "", ""})).isEqualTo(new GameStatus(GameState.ENDED, TicTacToeTeam.ROUND));
    }
}
