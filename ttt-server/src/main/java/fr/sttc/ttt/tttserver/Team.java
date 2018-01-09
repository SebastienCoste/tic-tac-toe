package fr.sttc.ttt.tttserver;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;

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

        return Arrays.asList(Team.values()).stream().filter(t -> t.letter.equals(letter))
                .findFirst().orElse(null);
    }
}
