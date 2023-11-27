package ru.kekw.rockpapperscissors.rps;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kekw.rockpapperscissors.FSM.RpsStateMachine;
import ru.kekw.rockpapperscissors.duel.DuelSession;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RpsResolverService implements RpsResolver {

    private final Map<String,RpsStateMachine> states;

    @Override
    public Result resolve(DuelSession session) {
        var one = session.getSessionPlayerOne().getPlayer().getMove();
        var two = session.getSessionPlayerTwo().getPlayer().getMove();
        if (one.equals(two)) {
            return new Result().setDraw(true).setFirstPlayerWin(false);
        }
        var secondPlayerState = states.get(two.toString());
        var firstPlayerWin = secondPlayerState.thisStateIsWin(one);
        return new Result().setFirstPlayerWin(firstPlayerWin);
    }
}
