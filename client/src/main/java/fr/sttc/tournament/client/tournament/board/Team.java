package fr.sttc.tournament.client.tournament.board;

import java.util.Arrays;

public interface Team {

    String letter = null;

    Team next();

    Team newTeam();

    String getLetter();

    Team[] allValues();

    default Team from(String letter) {

        return Arrays.stream(allValues()).filter(t -> t.letter != null && t.letter.equals(letter))
                .findFirst().orElse(null);
    }
}
