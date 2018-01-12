package fr.sttc.server.tictactoe;

import fr.sttc.server.tournament.board.Team;

import java.util.Arrays;

public enum TicTacToeTeam implements Team {

    CROSS("C"),
    ROUND("R");

    public String letter;

    public Team[] allValues(){
        return TicTacToeTeam.values();
    }

    TicTacToeTeam(String letter){
        this.letter = letter;
    }

    public TicTacToeTeam next(){
        return (this == TicTacToeTeam.CROSS ? ROUND : CROSS);
    }

    public TicTacToeTeam newTeam(){
        return (Math.floor(Math.random() *2) == 1? ROUND : CROSS);
    }

    public TicTacToeTeam from(String letter){

        return Arrays.stream(TicTacToeTeam.values()).filter(t -> t.letter.equals(letter))
                .findFirst().orElse(null);
    }
}
