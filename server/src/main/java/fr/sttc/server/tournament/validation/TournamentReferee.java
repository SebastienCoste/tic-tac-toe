package fr.sttc.server.tournament.validation;

public interface TournamentReferee {
    GameStatus evaluateBoard(String[] board);
}
