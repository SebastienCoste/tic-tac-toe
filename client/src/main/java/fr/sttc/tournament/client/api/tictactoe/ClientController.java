package fr.sttc.tournament.client.api.tictactoe;

import fr.sttc.tournament.client.tournament.TournamentManager;
import fr.sttc.tournament.client.tournament.game.Game;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(value= "/tournament/tictactoe", produces = MediaType.TEXT_PLAIN_VALUE)
public class ClientController {

    private final TournamentManager tournamentManager;

    public ClientController(TournamentManager tournamentManager) {
        this.tournamentManager = tournamentManager;
    }

    @RequestMapping(path = "/register/{gameId}/{team}/{url}", method = RequestMethod.GET)
    public Boolean register (@NotNull @PathVariable String gameId, @NotNull @PathVariable String team, @NotNull @PathVariable String url){

        return tournamentManager.register(Game.TICTACTOE, gameId, team, url);
    }

    @RequestMapping(path = "/start/{gameId}", method = RequestMethod.GET)
    public Boolean start (@NotNull @PathVariable String gameId){

        return tournamentManager.start(Game.TICTACTOE, gameId);
    }
}
