package fr.sttc.server.tournament.client;

import fr.sttc.server.api.TournamentApiClient;
import fr.sttc.server.tournament.board.Action;
import fr.sttc.server.tournament.board.Move;
import fr.sttc.server.tournament.board.ResultTournament;
import fr.sttc.server.tournament.board.Team;
import fr.sttc.server.tournament.game.Game;


public abstract class TournamentClient {

    private final String url;
    private Boolean active;
    public final String gameId;
    public final Team team;
    private final Game game;


    private final TournamentApiClient client = new TournamentApiClient();

    protected abstract Action getActionRepresentative();

    protected TournamentClient(String url, String gameId, Team team, Game game) {
        this.url = url;
        this.game = game;
        this.active = true;
        this.gameId = gameId;
        this.team = team;
    }


    public Action askForMove() {

        if (!active) {
            return null;
        }

        Action move = client.sendEvent(
                new EventClient(("http://" + url + "/" + game + "/ask/" + gameId).toLowerCase()),
                getActionRepresentative().getDeserializer()
        );
        if (move == null) {
            active = false;
        }
        return move;
    }

    public void tellMove(Move move) {

        if (!active) {
            return;
        }

        client.sendEvent(
                new EventClient(("http://" + url + "/" + game + "/move/" + move.gameId + "/" + move.team.toString().toLowerCase() + "/" + move.moveNumber +
                        "/" + move.position.serializeIt()).toLowerCase()),
                null);

    }

    public void tellResult(ResultTournament result) {

        if (!active) {
            return;
        }

        client.sendEvent(
                new EventClient(("http://" + url + "/" + game + "/result/" + gameId + "/" + result.toString().toLowerCase()).toLowerCase()),
                null);

        active = false;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TournamentClient that = (TournamentClient) o;

        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
