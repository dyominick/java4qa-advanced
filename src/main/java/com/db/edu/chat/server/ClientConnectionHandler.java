package com.db.edu.chat.server;

import java.io.IOException;
import java.util.Collection;

import com.db.edu.chat.connection.Connection;
import com.db.edu.chat.logics.BusinessLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientConnectionHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientConnectionHandler.class);

    private final Connection connection;
    private final Collection<Connection> realConnections;
    private BusinessLogic businessLogic;

    public ClientConnectionHandler(
        Connection clientConnection,
        Collection<Connection> clientConnections,
        BusinessLogic businessLogic) throws IOException {

        this.connection = clientConnection;
        this.realConnections = clientConnections;
        this.businessLogic = businessLogic;
    }

    @Override
    public void run() {
        while(true) {
            try {
                if(-1==businessLogic.handle())
                    break;
            } catch (IOException e) {
                LOGGER.error("Network reading message from socket " + connection, e);
                try {
                    connection.close();
                } catch (IOException innerE) {
                    LOGGER.debug("Error closing socket ", innerE);
                }
                LOGGER.error("Removing socket and stop this handler thread");
                realConnections.remove(connection);
                return;
            }

        }
    }
}
