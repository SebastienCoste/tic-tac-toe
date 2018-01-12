package fr.sttc.tournament.client.tournament.game;

import fr.sttc.tournament.client.tournament.board.Action;
import fr.sttc.tournament.client.tournament.board.Team;

public interface ActionManager {

    Action askForMove (String gameId);

    Boolean tellTheMove (String gameId,
                 Team team,
                 Integer moveNumber,
                 Action action);

    Boolean tellTheResult (String gameId, ResultTournament result);
}
