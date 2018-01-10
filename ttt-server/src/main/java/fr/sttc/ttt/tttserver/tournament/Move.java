package fr.sttc.ttt.tttserver.tournament;

public class Move {

    public String gameId;
    public Team team;
    public Integer position;
    public Integer moveNumber;

    public Move(String gameId, Team team, Integer position, Integer moveNumber) {
        this.gameId = gameId;
        this.team = team;
        this.position = position;
        this.moveNumber = moveNumber;
    }
}
