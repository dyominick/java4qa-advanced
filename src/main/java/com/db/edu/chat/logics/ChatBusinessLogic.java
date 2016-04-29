package com.db.edu.chat.logics;

import com.db.edu.chat.connection.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.SocketException;
import java.util.Collection;

@Component
public class ChatBusinessLogic implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatBusinessLogic.class);

    private Collection<Connection> connections;
    private Connection connection;

    public ChatBusinessLogic(Collection<Connection> connections, Connection connection) {
        this.connections = connections;
        this.connection = connection;
    }

    @Override
    public void run(){
        while(true) {
            try {
                handle();
            }
            catch (ClientDisconnectedException e) {
                LOGGER.warn("Client was disconnected: "+connection);
                connections.remove(connection);
                connection.close();
                break;
            }
            catch (FailedConnectionException e) {
                LOGGER.error("Client socket was closed: " + connection);
                connections.remove(connection);
                connection.close();
                break;
            }
        }
    }

    void handle() throws ClientDisconnectedException, FailedConnectionException{

        String message;
        try {
            message = connection.read();
        } catch (SocketException e) {
            throw new ClientDisconnectedException(e);
        }catch (IOException e) {
            throw new FailedConnectionException(e);
        }
        if(message == null){
            String nullMessageWarning = "Client "+ connection + "is disconnected.";
            notifyAll(nullMessageWarning);
            throw  new ClientDisconnectedException(nullMessageWarning);
        }
        notifyAll(message);
    }

    void notifyAll(String message) throws FailedConnectionException, ClientDisconnectedException {
        for (Connection outcomingConnection : connections)
            try {

                if (outcomingConnection.equals(connection))
                    continue;
                outcomingConnection.write(message);

            } catch (SocketException e) {
                throw new ClientDisconnectedException(e);
            }
            catch (IOException e) {
                LOGGER.error("Error writing message " + message + " to connection " + outcomingConnection + ". Closing socket", e);
                throw new FailedConnectionException(e);
            }

    }
}