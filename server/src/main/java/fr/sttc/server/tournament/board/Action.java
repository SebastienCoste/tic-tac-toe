package fr.sttc.server.tournament.board;

import java.util.function.Function;

public interface Action<T> {

    String serializeIt();

    Function<String, Action> getDeserializer();

    T value();
}
