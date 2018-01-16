package fr.sttc.server.tournament.board;

public class Move {

    public final String gameId;
    public final Team team;
    public final Action position;
    public final Integer moveNumber;

    public Move(String gameId, Team team, Action position, Integer moveNumber) {
        this.gameId = gameId;
        this.team = team;
        this.position = position;
        this.moveNumber = moveNumber;
    }
}
