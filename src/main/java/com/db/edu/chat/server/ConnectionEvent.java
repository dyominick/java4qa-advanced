package com.db.edu.chat.server;

import com.db.edu.chat.common.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Collection;


public class ConnectionEvent implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(ConnectionEvent.class);
    private volatile boolean isAlive=true;

    private volatile ServerSocket serverSocket;
    private final Collection<Connection> connections = new java.util.concurrent.CopyOnWriteArrayList<>();

    public ConnectionEvent(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void requestThreadStop() {
        isAlive = false;
    }

    @Override
    public void run() {
        while(isAlive) {
            try {
                Connection clientConnection = new RealConnection(serverSocket);
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
                logger.debug("Intentionally closed socket: time to stop",e);
                break;
            } catch (IOException e) {
                logger.error("Network error", e);
                break;
            }
        }
    }
}
