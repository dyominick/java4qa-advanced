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

    private final ServerSocket serverSocket;
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
                Connection incomingConnection = new RealServerConnection(serverSocket);
                incomingConnection.accept();
                connections.add(incomingConnection);
                Thread clientConnectionHandler = new Thread(
                        new ClientConnectionHandler(
                                incomingConnection,
                                connections,
                                new ChatBusinessLogic(connections)
                        )
                );
                clientConnectionHandler.setDaemon(true);
                clientConnectionHandler.start();

            } catch (SocketException e) {
                LOGGER.debug("Intentionally closed socket: time to stop");
                break;
            } catch (IOException e) {
                LOGGER.error("Network error", e);
                break;
            }
        }
    }
}
