package ru.kekw.rockpapperscissors.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;

@Slf4j
@Service
public class ConnectionService {
    private final ServerSocket serverSocket;
    private final ApplicationContext applicationContext;

    public ConnectionService(ApplicationContext context) throws IOException {
        this.applicationContext = context;
        serverSocket = new ServerSocket(23);
        serverStart();
    }

    private void serverStart() {
        try {
            // check and loop the serverSocket
            while (!serverSocket.isClosed()) {
                var socket = serverSocket.accept();
                log.info("New user connected");
                var session = applicationContext.getBean(GameSession.class);
                session.setPlayerSocket(socket);
                session.start();
            }
        } catch (IOException e) {
            log.error("Error was occurred when server started", e);
        }
    }
}
