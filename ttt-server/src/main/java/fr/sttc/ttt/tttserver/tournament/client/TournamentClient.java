package fr.sttc.ttt.tttserver.tournament.client;

import fr.sttc.ttt.tttserver.api.TournamentApiClient;
import fr.sttc.ttt.tttserver.tournament.board.Move;
import fr.sttc.ttt.tttserver.tournament.board.ResultTournament;
import fr.sttc.ttt.tttserver.tournament.board.Team;

import java.util.function.Function;

public class TournamentClient {

    private String url;
    private Boolean active;
    public String gameId;
    public Team team;

    private TournamentApiClient client = new TournamentApiClient();

    public TournamentClient(String url, String gameId, Team team){
        this.url = url;
        this.active = true;
        this.gameId = gameId;
        this.team = team;
    }


    public Integer askForMove(){

        if(!active){
            return null;
        }

        Integer move = client.sendEvent(new EventClient(url + "/ask/" + gameId), Integer::valueOf);
        if(move == null){
            active = false;
        }
        return move;
    }

    public void tellMove(Move move){

        if(!active){
            return;
        }

        client.sendEvent(
                new EventClient(url+"/move/" + move.gameId + "/" + move.team.toString().toLowerCase() + "/" + move.moveNumber + "/" + move.position),
                Function.identity());

    }

    public void tellResult(ResultTournament result){

        if(!active){
            return;
        }

        client.sendEvent(
                new EventClient(url+"/result/" + gameId + "/" + result.toString().toLowerCase()),
                Function.identity());

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
