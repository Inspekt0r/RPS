package ru.kekw.rockpapperscissors.duel;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.kekw.rockpapperscissors.server.GameSession;

import java.util.Objects;

@Data
@Accessors(chain = true)
public class DuelSession {
    private GameSession sessionPlayerOne;
    private GameSession sessionPlayerTwo;

    public boolean hasTwoPlayer() {
        return Objects.nonNull(this.sessionPlayerOne) && Objects.nonNull(this.sessionPlayerTwo);
    }

    public boolean hasEmptySlot() {
        return Objects.isNull(this.sessionPlayerTwo) || Objects.isNull(this.sessionPlayerOne);
    }

    public DuelSession putPlayerToAnyEmptySlot(GameSession player) {
        if (Objects.isNull(this.sessionPlayerTwo) && !player.equals(this.sessionPlayerOne)) {
            this.sessionPlayerTwo = player;
            return this;
        }
        if (Objects.isNull(this.sessionPlayerOne) && !player.equals(this.sessionPlayerTwo)) {
            this.sessionPlayerOne = player;
            return this;
        }
        return null;
    }

    //todo toString and maybe GUID idk
}
