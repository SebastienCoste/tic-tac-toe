package fr.sttc.ttt.tttserver.tournament.board;

import java.util.Arrays;

public enum Team {

    CROSS("C"),
    ROUND("R");

    public String letter;

    Team(String letter){
        this.letter = letter;
    }

    public Team next(){
        return (this == Team.CROSS ? ROUND : CROSS);
    }

    public static Team newTeam(){
        return (Math.floor(Math.random() *2) == 1? ROUND : CROSS);
    }

    public static Team from(String letter){

        return Arrays.stream(Team.values()).filter(t -> t.letter.equals(letter))
                .findFirst().orElse(null);
    }
}
