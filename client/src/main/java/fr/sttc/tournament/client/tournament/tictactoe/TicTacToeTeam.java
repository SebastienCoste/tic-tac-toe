package fr.sttc.tournament.client.tournament.tictactoe;


import fr.sttc.tournament.client.tournament.board.Team;

import java.util.Arrays;

public enum TicTacToeTeam implements Team {

    CROSS("C"),
    ROUND("R");

    public String letter;

    public String getLetter() {
        return this.letter;
    }

    public Team[] allValues() {
        return TicTacToeTeam.values();
    }

    TicTacToeTeam(String letter) {
        this.letter = letter;
    }

    public TicTacToeTeam next() {
        return (this == TicTacToeTeam.CROSS ? ROUND : CROSS);
    }

    public TicTacToeTeam newTeam() {
        return (Math.floor(Math.random() * 2) == 1 ? ROUND : CROSS);
    }

    public static TicTacToeTeam fromName(String name) {

        return Arrays.stream(TicTacToeTeam.values()).filter(t -> t.toString().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public TicTacToeTeam from(String letter) {

        return Arrays.stream(TicTacToeTeam.values()).filter(t -> t.letter.equalsIgnoreCase(letter))
                .findFirst().orElse(null);
    }
}
