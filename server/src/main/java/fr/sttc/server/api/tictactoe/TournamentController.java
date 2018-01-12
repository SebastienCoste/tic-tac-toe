package fr.sttc.server.api.tictactoe;


import fr.sttc.server.tournament.game.Game;
import fr.sttc.server.tournament.register.TournamentManager;
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
public class TournamentController {

    private final TournamentManager tournamentManager;

    private final static Logger logger = LoggerFactory.getLogger(TournamentController.class);

    public TournamentController(TournamentManager tournamentManager) {
        this.tournamentManager = tournamentManager;
    }

    @RequestMapping(path = "/register/{gameId}/{team}/{clientUrl}", method = RequestMethod.GET)
    public Boolean register (@NotNull @PathVariable String gameId, @NotNull @PathVariable String team, @NotNull @PathVariable String clientUrl){

        logger.info(String.format("/tournament/tictactoe/register/%s/%s/%s",
                gameId, team, clientUrl
        ));
        Boolean register = tournamentManager.register(Game.TICTACTOE, gameId, team, clientUrl);
        logger.info(String.format("/tournament/tictactoe/register/%s/%s/%s : %s",
                gameId, team, clientUrl, register.toString()
        ));
        return register;
    }

    @RequestMapping(path = "/start/{gameId}", method = RequestMethod.GET)
    public Boolean start (@NotNull @PathVariable String gameId){
        logger.info(String.format("/tournament/tictactoe/start/%s",
                gameId
        ));
        Boolean started = tournamentManager.start(Game.TICTACTOE, gameId);
        logger.info(String.format("/tournament/tictactoe/start/%s : %s",
                gameId, started
        ));
        return started;
    }
}
