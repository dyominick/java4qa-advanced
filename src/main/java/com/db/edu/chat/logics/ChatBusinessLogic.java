package com.db.edu.chat.logics;

import com.db.edu.chat.connection.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collection;

public class ChatBusinessLogic implements BusinessLogic {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatBusinessLogic.class);

    private Connection incomingConnection;
    private Collection<Connection> connections;

    public ChatBusinessLogic(Connection incoming, Collection<Connection> connections) {
        this.incomingConnection = incoming;
        this.connections = connections;
    }

    @Override
    public void handle() throws ClientDisconnectedException, FailedConnectionException{

        String message;
        try {
            message = incomingConnection.read();
        } catch (IOException e) {
            throw new FailedConnectionException(e);
        }
        if(message == null){
            String nullMessageWarning = "Client "+incomingConnection + "is disconnected.";
            notifyAll(nullMessageWarning);
            throw  new ClientDisconnectedException(nullMessageWarning);
        }
        notifyAll(message);
    }

    private void notifyAll(String message) throws FailedConnectionException{
        for (Connection outcomingConnection : connections)
            try {

                if (outcomingConnection == this.incomingConnection)
                    continue;

                outcomingConnection.write(message);

            } catch (IOException e) {
                LOGGER.error("Error writing message " + message + " to connection " + outcomingConnection + ". Closing socket", e);
                try {
                    incomingConnection.write("Client disconnected " + outcomingConnection);
                } catch (IOException e1) {
                    throw new FailedConnectionException(e1);
                }
                outcomingConnection.close();
                connections.remove(outcomingConnection);
                throw new FailedConnectionException(e);
            }

    }
}