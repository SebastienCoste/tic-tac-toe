package fr.sttc.server.tictactoe;

import fr.sttc.server.tournament.board.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class TicTacToeAction implements Action {

    private final static Logger logger = LoggerFactory.getLogger(TicTacToeAction.class);

    public static final TicTacToeAction EMPTY = new TicTacToeAction(null);

    private final Integer position;

    public TicTacToeAction(Integer position) {
        this.position = position;
    }

    @Override
    public String serializeIt() {
        return position.toString();
    }

    @Override
    public Integer value() {
        return position;
    }

    @Override
    public Function<String, TicTacToeAction> getDeserializer() {
        return s -> {
            Integer pos = null;
            try {
                pos = s == null ? -1 : Integer.valueOf(s);
            } catch (NumberFormatException nfe) {
                logger.error(nfe.getMessage());
            }
            return new TicTacToeAction(pos);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicTacToeAction ticTacToeAction = (TicTacToeAction) o;

        return position != null ? position.equals(ticTacToeAction.position) : ticTacToeAction.position == null;
    }

    @Override
    public int hashCode() {
        return position != null ? position.hashCode() : 0;
    }
}
