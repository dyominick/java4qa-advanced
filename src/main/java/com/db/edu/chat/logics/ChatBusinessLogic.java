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
    public int handle() {
        String message= null;
        try {
            message = incomingConnection.read();
        } catch (IOException e) {
            LOGGER.error("Error while reading message from incoming connection: ", e);
            incomingConnection.close();
        }
        if(message == null){
            connections.remove(incomingConnection);
            return -1;
        }

        for (Connection outcomingConnection : connections) {
            try {

                if (outcomingConnection == this.incomingConnection)
                    continue;

                outcomingConnection.write(message);

            } catch (IOException e) {
                LOGGER.error("Error writing message " + message + " to connection " + outcomingConnection + ". Closing socket", e);
                outcomingConnection.close();
                LOGGER.error("Removing connection " + outcomingConnection);
                connections.remove(outcomingConnection);
            }
        }
        return 0;
    }
}