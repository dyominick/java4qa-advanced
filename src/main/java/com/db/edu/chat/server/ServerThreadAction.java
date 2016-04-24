package com.db.edu.chat.server;

import com.db.edu.chat.connection.Connection;
import com.db.edu.chat.connection.RealServerConnection;
import com.db.edu.chat.logics.ChatBusinessLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Collection;


public class ServerThreadAction implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerThreadAction.class);
    private volatile boolean isAlive=true;

    private volatile ServerSocket serverSocket;
    private final Collection<Connection> connections = new java.util.concurrent.CopyOnWriteArrayList<>();

    public ServerThreadAction(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void requestStop() {
        isAlive = false;
    }

    @Override
    public void run() {
        while(isAlive) {
            try {
                Connection clientConnection = new RealServerConnection(serverSocket);
                connections.add(clientConnection);
                Thread clientConnectionHandler = new Thread(
                        new ClientConnectionHandler(
                                clientConnection,
                                connections,
                                new ChatBusinessLogic(clientConnection, connections)
                        )
                );
                clientConnectionHandler.setDaemon(true);
                clientConnectionHandler.start();
            } catch (SocketException e) {
                LOGGER.debug("Intentionally closed socket: time to stop",e);
                break;
            } catch (IOException e) {
                LOGGER.error("Network error", e);
                break;
            }
        }
    }
}
