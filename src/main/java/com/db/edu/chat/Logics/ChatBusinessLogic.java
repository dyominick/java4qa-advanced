package com.db.edu.chat.Logics;

import com.db.edu.chat.Connection.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collection;

public class ChatBusinessLogic implements BusinessLogic {
    private static final Logger logger = LoggerFactory.getLogger(ChatBusinessLogic.class);

    private Connection connection;
    private Collection<Connection> connections;

    public ChatBusinessLogic(Connection realConnection, Collection<Connection> connections) {
        this.connection = realConnection;
        this.connections = connections;
    }

    @Override
    public int handle() throws IOException{
        String message=connection.read();
        if(message == null) return -1;



        for (Connection outConnection : connections) {
            try {

                if (outConnection == this.connection) continue;

                outConnection.write(message);

            } catch (IOException e) {
                logger.error("Error writing message " + message + " to connection " + outConnection + ". Closing socket", e);
                try {
                    outConnection.close();
                } catch (IOException innerE) {
                    logger.error("Error closing socket ", innerE);
                }

                logger.error("Removing connection " + outConnection);
                connections.remove(outConnection);
            }

        }

        return 0;
    }
}
