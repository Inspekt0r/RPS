package ru.kekw.rockpapperscissors.FSM;

import org.springframework.stereotype.Component;
import ru.kekw.rockpapperscissors.move.MoveSelection;

@Component(value = "SCISSORS")
public class RpsStateMachineScissors implements RpsStateMachine {
    @Override
    public String getState() {
        return MoveSelection.SCISSORS.toString();
    }

    @Override
    public boolean thisStateIsWin(MoveSelection defence) {
        switch (defence) {
            case ROCK -> {
                return false;
            }
            case PAPER -> {
                return true;
            }
        }
        throw new IllegalArgumentException("New state error");
    }
}
