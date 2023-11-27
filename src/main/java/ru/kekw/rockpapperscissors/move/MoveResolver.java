package ru.kekw.rockpapperscissors.move;

import java.io.IOException;

public interface MoveResolver {
    void resolve(String message) throws IOException;
}
