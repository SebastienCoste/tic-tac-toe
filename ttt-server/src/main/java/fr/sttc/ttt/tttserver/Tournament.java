package fr.sttc.ttt.tttserver;

import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class Tournament {

    public Set<String> cross = new HashSet<>();
    public Set<String> round = new HashSet<>();
    public boolean isStarted = false;
    public String gameId;

    public Tournament (String gameId){
        this.gameId = gameId;
    }

    public boolean start(){

        if(isStarted || cross.size() + round.size() <2){
            return false;
        }

        if(cross.size() == 0){
            long switchPlayer = Math.round(Math.random() * round.size());
            cross.add(nthElement(round, switchPlayer));
        }

        if(round.size() == 0){
            long switchPlayer = Math.round(Math.random() * cross.size());
            round.add(nthElement(cross, switchPlayer));
        }

        isStarted = true;

        run();
        return true;
    }

    private void run() {
        RunnableTournament runner = new RunnableTournament(cross, round, gameId);
        runner.run();
    }

    public synchronized boolean register(String team, String url) {
        if (!isStarted || StringUtils.isEmpty(team) || StringUtils.isEmpty(url)) {
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
        if(round.contains(url) || cross.contains(url)){
            return false;
        }
        return cross.add(url);

    }

    private boolean registerRound(String url) {
        if(round.contains(url) || cross.contains(url)){
            return false;
        }
        return round.add(url);
    }

    private String nthElement(Set<String>data, long n){
        long index = 0;
        for(String element : data){
            if(index == n){
                data.remove(element);
                return element;
            }
            index++;
        }
        return null;
    }


}
