package fr.sttc.tournament.client.api.tictactoe;

import fr.sttc.tournament.client.tournament.board.Team;
import fr.sttc.tournament.client.tournament.game.ResultTournament;
import fr.sttc.tournament.client.tournament.tictactoe.TicTacToeAction;
import fr.sttc.tournament.client.tournament.tictactoe.TicTacToeActionManager;
import fr.sttc.tournament.client.tournament.tictactoe.TicTacToeTeam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(value= "/tictactoe", produces = MediaType.TEXT_PLAIN_VALUE)
public class GameController {

    private final static Logger logger = LoggerFactory.getLogger(GameController.class);

    private final TicTacToeActionManager ticTacToeActionManager;

    public GameController(TicTacToeActionManager ticTacToeActionManager) {
        this.ticTacToeActionManager = ticTacToeActionManager;
    }

    @RequestMapping(path = "/ask/{gameId}", method = RequestMethod.GET)
    public String askForMove (@NotNull @PathVariable String gameId){

        logger.info(String.format("/tictactoe/ask/%s", gameId));

        String serializedResult = ticTacToeActionManager.askForMove(gameId).serializeIt();

        logger.info(String.format("/tictactoe/ask/%s : %s", gameId, serializedResult));
        return serializedResult;
    }

    @RequestMapping(path = "/move/{gameId}/{team}/{moveNumber}/{serializedMove}", method = RequestMethod.GET)
    public Boolean tellTheMove (@NotNull @PathVariable String gameId,
                                @NotNull @PathVariable String team,
                                @NotNull @PathVariable Integer moveNumber,
                                @NotNull @PathVariable String serializedMove){

        logger.info(String.format("/tictactoe/move/%s/%s/%s/%s",
                gameId, team.toString(), moveNumber.toString(), serializedMove
        ));
        Boolean received = ticTacToeActionManager.tellTheMove(gameId, TicTacToeTeam.fromName(team), moveNumber, TicTacToeAction.EMPTY.getDeserializer().apply(serializedMove));

        logger.info(String.format("/tictactoe/move/%s/%s/%s/%s : %s",
                gameId, team.toString(), moveNumber.toString(), serializedMove, received.toString()
        ));
        return received;
    }

    @RequestMapping(path = "/result/{gameId}/{result}", method = RequestMethod.GET)
    public Boolean tellTheResult (@NotNull @PathVariable String gameId, @NotNull @PathVariable String result){

        logger.info(String.format("/tictactoe/result/%s/%s",
                gameId, result
        ));
        Boolean received = ticTacToeActionManager.tellTheResult(gameId, ResultTournament.valueOf(result.toUpperCase()));
        logger.info(String.format("/tictactoe/result/%s/%s : %s",
                gameId, result.toString(), received.toString()
        ));
        return received;
    }
}
