package ru.kekw.rockpapperscissors.duel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kekw.rockpapperscissors.rps.RpsResolver;
import ru.kekw.rockpapperscissors.server.GameSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@Service
public class DuelService {
    private final List<DuelSession> duelSessions;
    private final RpsResolver service;

    public DuelService(RpsResolver resolver) {
        this.duelSessions = Collections.synchronizedList(new ArrayList<>());
        service = resolver;
    }

    public synchronized void putPlayerToSession(GameSession player) {
        var session = duelSessions.stream()
            .filter(DuelSession::hasEmptySlot)
            .findFirst()
            .map(duelSession -> duelSession.putPlayerToAnyEmptySlot(player));
        if (session.isEmpty()) {
            var newSession = new DuelSession().setSessionPlayerOne(player);
            duelSessions.add(newSession);
        }
        player.getPlayer().setPlayerInSession(true);
    }

    public Optional<DuelSession> sessionIsReady(GameSession session) {
        return duelSessions.stream().filter(isReady(session)).findFirst();
    }

    public boolean allPlayerMakeTurn(DuelSession duelSession) throws IOException {
        if(playersHasMakeMove(duelSession)) {
            calculateResult(duelSession);
            return true;
        } else {
            return false;
        }
    }

    private synchronized void removeDuelSession(DuelSession duelSession) {
        var session = duelSessions.remove(duelSession);
        duelSession.getSessionPlayerTwo().getPlayer().setPlayerInSession(false);
        duelSession.getSessionPlayerOne().getPlayer().setPlayerInSession(false);
        log.info("Session {} was removed after successfully calc", session);
    }

    public synchronized void calculateResult(DuelSession duel) throws IOException {
        var result = service.resolve(duel);
        var playerOneSession = duel.getSessionPlayerOne();
        var playerTwoSession = duel.getSessionPlayerTwo();

        var playerOneName = playerOneSession.getPlayer().getName();
        var playerTwoName = playerTwoSession.getPlayer().getName();

        if (Boolean.TRUE.equals(result.getDraw())) {
            playerOneSession.sendMessage("DRAW!!! Try again");
            removeMoveState(playerOneSession);
            playerTwoSession.sendMessage("DRAW!!! Try again");
            removeMoveState(playerTwoSession);
            return;
        }
        if (Boolean.TRUE.equals(result.getFirstPlayerWin())) { //todo fix this caused if null this is suck)
            playerOneSession.sendMessage(gratsMsg(playerTwoName));
            playerTwoSession.sendMessage(lostMsg(playerOneName));
        } else {
            playerTwoSession.sendMessage(gratsMsg(playerOneName));
            playerOneSession.sendMessage(lostMsg(playerTwoName));
        }
        removeDuelSession(duel);
    }

    private void removeMoveState(GameSession playerOneSession) {
        playerOneSession.getPlayer().setMove(null);
    }

    private String gratsMsg(String lostName) {
        return String.format("Congratulations!!! You win %s!", lostName);
    }

    private String lostMsg(String winnerName) {
        return String.format("Your lost, %s beat you :(", winnerName);
    }

    private Predicate<DuelSession> isReady(GameSession session) {
        return ds -> ds.hasTwoPlayer() &&
            (session.equals(ds.getSessionPlayerOne()) || session.equals(ds.getSessionPlayerTwo()));
    }

    private boolean playersHasMakeMove(DuelSession session) {
        return session.getSessionPlayerOne().getPlayer().getMove() != null
             && session.getSessionPlayerTwo().getPlayer().getMove() != null;
    }
}
