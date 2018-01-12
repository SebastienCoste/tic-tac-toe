package fr.sttc.tournament.client.api.tictactoe;

import fr.sttc.tournament.client.tournament.board.Team;
import fr.sttc.tournament.client.tournament.game.ResultTournament;
import fr.sttc.tournament.client.tournament.tictactoe.TicTacToeAction;
import fr.sttc.tournament.client.tournament.tictactoe.TicTacToeActionManager;
import fr.sttc.tournament.client.tournament.tictactoe.TicTacToeTeam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(value= "/tictactoe", produces = MediaType.TEXT_PLAIN_VALUE)
public class GameController {

    private final TicTacToeActionManager ticTacToeActionManager;

    public GameController(TicTacToeActionManager ticTacToeActionManager) {
        this.ticTacToeActionManager = ticTacToeActionManager;
    }

    @RequestMapping(path = "/ask/{gameId}", method = RequestMethod.GET)
    public String askForMove (@NotNull @PathVariable String gameId){
        return ticTacToeActionManager.askForMove(gameId).serializeIt();
    }

    @RequestMapping(path = "/move/{gameId}/{team}/{moveNumber}/{serializedMove}", method = RequestMethod.GET)
    public Boolean tellTheMove (@NotNull @PathVariable String gameId,
                                @NotNull @PathVariable TicTacToeTeam team,
                                @NotNull @PathVariable Integer moveNumber,
                                @NotNull @PathVariable String serializedMove){
        return ticTacToeActionManager.tellTheMove(gameId, team, moveNumber, TicTacToeAction.EMPTY.getDeserializer().apply(serializedMove));
    }

    @RequestMapping(path = "/result/{gameId}/{result}", method = RequestMethod.GET)
    public Boolean tellTheResult (@NotNull @PathVariable String gameId, @NotNull @PathVariable ResultTournament result){

        return ticTacToeActionManager.tellTheResult(gameId, result);
    }
}
