package ru.kekw.rockpapperscissors.FSM;

import ru.kekw.rockpapperscissors.move.MoveSelection;

public interface RpsStateMachine {
    String getState();

    boolean thisStateIsWin(MoveSelection defence);
}
