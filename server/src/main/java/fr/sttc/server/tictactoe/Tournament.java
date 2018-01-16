package fr.sttc.server.tictactoe;

import fr.sttc.server.api.TournamentRunnable;
import fr.sttc.server.api.tictactoe.TicTacToeRunnableRunnable;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class Tournament {

    private final Set<String> cross = new HashSet<>();
    private final Set<String> round = new HashSet<>();
    private boolean isStarted = false;
    private final String gameId;

    public Tournament(String gameId) {
        this.gameId = gameId;
    }

    public boolean start() {

        if (isStarted || cross.size() == 0 || round.size() == 0) {
            return false;
        }
        isStarted = true;
        run();
        return true;
    }

    private void run() {
        TournamentRunnable runner = new TicTacToeRunnableRunnable(cross, round, gameId);
        runner.run();
    }

    public synchronized boolean register(String team, String url) {
        if (isStarted || StringUtils.isEmpty(team) || StringUtils.isEmpty(url)) {
            return false;
        }

        switch (team.toLowerCase()) {
            case "round":
                return registerRound(url);
            case "cross":
                return registerCross(url);
            default:
                return false;
        }
    }

    private boolean registerCross(String url) {
        return !round.contains(url) && !cross.contains(url) && cross.add(url);
    }

    private boolean registerRound(String url) {
        return !round.contains(url) && !cross.contains(url) && round.add(url);
    }

}
