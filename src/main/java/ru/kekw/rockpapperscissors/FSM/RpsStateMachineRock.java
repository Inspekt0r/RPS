package ru.kekw.rockpapperscissors.FSM;

import org.springframework.stereotype.Component;
import ru.kekw.rockpapperscissors.move.MoveSelection;

@Component(value = "ROCK")
public class RpsStateMachineRock implements RpsStateMachine {
    @Override
    public String getState() {
        return MoveSelection.ROCK.toString();
    }

    @Override
    public boolean thisStateIsWin(MoveSelection defence) {
        switch (defence) {
            case PAPER -> {
                return false;
            }
            case SCISSORS -> {
                return true;
            }
        }
        throw new IllegalArgumentException("New state error");
    }
}
