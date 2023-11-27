package ru.kekw.rockpapperscissors.server;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.kekw.rockpapperscissors.duel.DuelService;
import ru.kekw.rockpapperscissors.move.MoveResolver;
import ru.kekw.rockpapperscissors.move.MoveResolverService;
import ru.kekw.rockpapperscissors.move.MoveSelection;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Getter
@Setter
@Component
@Scope("prototype")
@RequiredArgsConstructor
public class GameSession extends Thread {
    private UUID uuid;
    private Socket playerSocket;
    private Player player;
    private BufferedWriter output;
    private MoveResolver resolver;
    private Boolean isRunning = true;
    private final DuelService duelService;

    @Override
    public void run() {
        this.uuid = UUID.randomUUID();
        resolver = new MoveResolverService(this);
        try {
            output = getPlayerOutput(playerSocket);

            log.debug("User connected");
            var wellcumMessage = "Hello! This is RPS Game! Please entry your nickname:\r";
            sendMessage(wellcumMessage);
            log.info("Welcome message send successfully!");
            var firstRun = true;
            while (isRunning) {
                var input = getPlayerInput(playerSocket);
                StringBuilder buffer = new StringBuilder();
                buffer.append(input.readLine());
                if (!"null".equals(buffer.toString())) {
                    var message = buffer.toString();
                    if (firstRun) {
                        firstRun = false;
                        message = message.substring(21); //wtf?! O o
                        player = new Player(message);
                        sendMessage(
                            String.format("Okay, your nickname is %s\n w8 your opponent", player.getName()));
                        duelService.putPlayerToSession(this);
                    } else {
                        if (!player.isPlayerInSession()) {
                            duelService.putPlayerToSession(this);
                        }
                        waitOpponent(message);

                    }
                    log.info("Get message: {}", message);


                    buffer = new StringBuilder();
                } else {
                    isRunning = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.debug("User {} disconnected", player.getName());
    }

    private void waitOpponent(String message) throws IOException {
        resolver.resolve(message);
        var duelSession = duelService.sessionIsReady(this);
        if (duelSession.isPresent()) {
            sendMessage("Your opponent is ready, " +
                "you can select move: R (Rock), P (Papper) or S (Scissors) and w8 opponent turn");
            var ds = duelSession.get();
            if (duelService.allPlayerMakeTurn(ds)) {
                log.info("Players {} and {} in duel sessions make turns and get results",
                    ds.getSessionPlayerOne().getPlayer().getName(),
                    ds.getSessionPlayerTwo().getPlayer().getName());
            }
        } else {
            sendMessage("Please w8 opponent!");
        }
    }

    public void sendMessage(String message) throws IOException {
        output.write(message);
        output.newLine();
        output.flush();
    }

    private BufferedReader getPlayerInput(Socket socket) throws IOException {
        var socketInputStream = socket.getInputStream();
        return new BufferedReader(new InputStreamReader(socketInputStream, StandardCharsets.UTF_8));
    }

    private BufferedWriter getPlayerOutput(Socket socket) throws IOException {
        var socketOutputStream = socket.getOutputStream();
        return new BufferedWriter(new OutputStreamWriter(socketOutputStream));
    }

    public void setupMove(MoveSelection move) {
        this.player.setMove(move);
    }

    @Data
    public static class Player {
        private boolean playerInSession = false;
        private final String name;
        private MoveSelection move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameSession session = (GameSession) o;
        return Objects.equals(uuid, session.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
