package fr.sttc.tournament.client.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value= "/tournament/tictactoe", produces = MediaType.TEXT_PLAIN_VALUE)
public class GameController {
}
