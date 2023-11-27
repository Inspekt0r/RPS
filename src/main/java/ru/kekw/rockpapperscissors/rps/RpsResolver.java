package ru.kekw.rockpapperscissors.rps;

import ru.kekw.rockpapperscissors.duel.DuelSession;

public interface RpsResolver {

    Result resolve(DuelSession session);
}
