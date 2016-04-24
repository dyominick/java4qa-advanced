package com.db.edu.chat.server;

import java.io.IOException;
import java.util.Collection;

import com.db.edu.chat.Connection.Connection;
import com.db.edu.chat.Logics.BusinessLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientConnectionHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ClientConnectionHandler.class);

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
                if(-1==businessLogic.handle()) break;
            } catch (IOException e) {
                logger.error("Network reading message from socket " + connection, e);
                try {
                    connection.close();
                } catch (IOException innerE) {
                    logger.debug("Error closing socket ", innerE);
                }
                logger.error("Removing socket and stop this handler thread");
                realConnections.remove(connection);
                return;
            }

        }
    }
}
