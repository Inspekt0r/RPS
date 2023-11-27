package ru.kekw.rockpapperscissors.FSM;

import org.springframework.stereotype.Component;
import ru.kekw.rockpapperscissors.move.MoveSelection;

@Component(value = "PAPER")
public class RpsStateMachinePaper implements RpsStateMachine {

    @Override
    public String getState() {
        return MoveSelection.PAPER.toString();
    }

    @Override
    public boolean thisStateIsWin(MoveSelection defence) {
        switch (defence) {
            case SCISSORS -> {
                return false;
            }
            case ROCK -> {
                return true;
            }
        }
        throw new IllegalArgumentException("New state error");
    }
}
