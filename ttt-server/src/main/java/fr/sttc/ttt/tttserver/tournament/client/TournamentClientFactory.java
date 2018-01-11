package fr.sttc.ttt.tttserver.tournament.client;

import fr.sttc.ttt.tttserver.tournament.board.Team;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TournamentClientFactory {

    public static TournamentClient buildClient (String url, String gameId, Team team){

        return new TournamentClient(url, gameId, team);
    }

    public static List<TournamentClient> buildClients(Collection<String> urls, String gameId, Team team){

        return urls.stream().map(url -> buildClient(url, gameId, team)).collect(Collectors.toList());
    }
}
