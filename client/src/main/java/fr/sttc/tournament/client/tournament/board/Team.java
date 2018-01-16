package fr.sttc.tournament.client.tournament.board;

import java.util.Arrays;

public interface Team {

    Team next();

    Team newTeam();

    String getLetter();

    Team[] allValues();

    default Team from(String letter) {

        return Arrays.stream(allValues()).filter(t -> t.getLetter() != null && t.getLetter().equals(letter))
                .findFirst().orElse(null);
    }
}
