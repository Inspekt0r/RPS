package ru.kekw.rockpapperscissors.rps;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Result {
    private Boolean draw;
    private Boolean firstPlayerWin;
}
