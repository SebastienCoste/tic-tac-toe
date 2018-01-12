package fr.sttc.tournament.client.api.tictactoe;

import fr.sttc.tournament.client.tournament.ClientManager;
import fr.sttc.tournament.client.tournament.game.Game;
import fr.sttc.tournament.client.tournament.tictactoe.TicTacToeActionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(value= "/tournament/tictactoe", produces = MediaType.TEXT_PLAIN_VALUE)
public class ClientController {

    private final static Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientManager clientManager;
    private final TicTacToeActionManager ticTacToeActionManager;

    public ClientController(ClientManager clientManager, TicTacToeActionManager ticTacToeActionManager) {
        this.clientManager = clientManager;
        this.ticTacToeActionManager = ticTacToeActionManager;
    }

    @RequestMapping(path = "/register/{gameId}/{team}/{serverUrl}", method = RequestMethod.GET)
    public Boolean register (@NotNull @PathVariable String gameId, @NotNull @PathVariable String team, @NotNull @PathVariable String serverUrl){
        logger.info(String.format("/tournament/tictactoe/register/%s/%s/%s",
                gameId, team, serverUrl
                ));
        Boolean register = clientManager.register(Game.TICTACTOE, gameId, team, serverUrl);
        register = register ? ticTacToeActionManager.createBoard(gameId) : false;
        logger.info(String.format("/tournament/tictactoe/register/%s/%s/%s : %s",
                gameId, team, serverUrl, register.toString()
        ));
        return register;
    }

    @RequestMapping(path = "/start/{gameId}/{serverUrl}", method = RequestMethod.GET)
    public Boolean start (@NotNull @PathVariable String gameId, @NotNull @PathVariable String serverUrl){
        logger.info(String.format("/tournament/tictactoe/start/%s",
                gameId
        ));
        Boolean started = clientManager.start(Game.TICTACTOE, gameId, serverUrl);
        logger.info(String.format("/tournament/tictactoe/start/%s : %s",
                gameId, started
        ));
        return started;
    }
}
