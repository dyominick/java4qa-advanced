package com.db.edu.chat.server;

import java.io.IOException;
import java.util.Collection;

import com.db.edu.chat.connection.Connection;
import com.db.edu.chat.logics.FailedConnectionException;
import com.db.edu.chat.logics.ClientDisconnectedException;
import com.db.edu.chat.logics.BusinessLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientConnectionHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientConnectionHandler.class);

    private final Connection connection;
    private final Collection<Connection> connections;
    private BusinessLogic businessLogic;

    public ClientConnectionHandler(
        Connection clientConnection,
        Collection<Connection> clientConnections,
        BusinessLogic businessLogic) throws IOException {

        this.connection = clientConnection;
        this.connections = clientConnections;
        this.businessLogic = businessLogic;
    }

    @Override
    public void run() {
        while(true) {
            try {
                businessLogic.handle(connection);
            }
            catch (ClientDisconnectedException e) {
                LOGGER.warn("Null message received");
                connections.remove(connection);
                connection.close();
                break;
            }
            catch (FailedConnectionException e) {
                LOGGER.error("Connection error with " + connection + ": ", e);
                connections.remove(connection);
                connection.close();
                break;
            }
        }
    }
}
