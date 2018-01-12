package fr.sttc.tournament.client.tournament.board;

import java.util.function.Function;

public interface Action<T> {

    String serializeIt();

    Function<String, Action> getDeserializer();

    T value();
}
