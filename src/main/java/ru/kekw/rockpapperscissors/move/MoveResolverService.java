package ru.kekw.rockpapperscissors.move;


import lombok.RequiredArgsConstructor;
import ru.kekw.rockpapperscissors.server.GameSession;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

@RequiredArgsConstructor
public final class MoveResolverService implements MoveResolver {
    private final GameSession gameSession;

    @Override
    public void resolve(String message) throws IOException {
        MoveSelection move = null;
        try {
            move = MoveSelection.getByShortName(message);
            if (move == null) {
                message = message.toUpperCase(Locale.ROOT);
                move = MoveSelection.valueOf(message);
            }
            gameSession.setupMove(move);
            gameSession.sendMessage(String.format("Your choices is %s, w8 opponent...", move));
        } catch (IllegalArgumentException ex) {
            gameSession.sendMessage("Value is not correct, try again: rock, papper or scissors.\n" +
                "Also you can send \"R\" \"P\" or \"S\"");
        }

    }

    public GameSession gameSession() {
        return gameSession;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (MoveResolverService) obj;
        return Objects.equals(this.gameSession, that.gameSession);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameSession);
    }

    @Override
    public String toString() {
        return "MoveResolverService[" +
            "gameSession=" + gameSession + ']';
    }

}
