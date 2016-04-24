package com.db.edu.chat.client;

import com.db.edu.chat.connection.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class ClientThreadAction implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientThreadAction.class);
    private Connection networkConnection;
    private Connection consoleConnection;

    public ClientThreadAction(Connection networkConnection, Connection consoleConnection){
        this.networkConnection = networkConnection;
        this.consoleConnection = consoleConnection;
    }

    @Override
    public void run() {
        while(true) {
            try {
                String message = networkConnection.read();
                if(message != null)
                    consoleConnection.write(message);
            }
            catch (IOException e) {
                LOGGER.error("IO exception: ", e);
                networkConnection.close();
                consoleConnection.close();

            }
        }
    }
}
