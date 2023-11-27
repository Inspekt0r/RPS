package ru.kekw.rockpapperscissors.move;

import java.util.Arrays;
import java.util.Locale;

public enum MoveSelection {
    ROCK("R"),
    PAPER("P"),
    SCISSORS("S");

    private String shortName;

    MoveSelection(String str) {
        shortName = str;
    }

    public String getShortName() {
        return shortName;
    }

    public static MoveSelection getByShortName(String str) {
        return Arrays.stream(MoveSelection.values())
            .filter(move -> move.getShortName().equals(str.toUpperCase(Locale.ROOT)))
            .findFirst()
            .orElse(null);
    }
}
