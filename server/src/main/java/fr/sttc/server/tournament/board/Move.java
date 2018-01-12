package fr.sttc.server.tournament.board;

public class Move {

    public String gameId;
    public Team team;
    public Action position;
    public Integer moveNumber;

    public Move(String gameId, Team team, Action position, Integer moveNumber) {
        this.gameId = gameId;
        this.team = team;
        this.position = position;
        this.moveNumber = moveNumber;
    }
}
