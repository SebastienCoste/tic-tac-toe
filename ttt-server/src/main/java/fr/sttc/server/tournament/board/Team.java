package fr.sttc.server.tournament.board;

import java.util.Arrays;

public interface Team {

    String letter = null;

    Team next();

    Team newTeam();

    Team[] allValues();

    default Team from(String letter){

        return Arrays.stream(allValues()).filter(t -> t.letter.equals(letter))
                .findFirst().orElse(null);
    }
}
