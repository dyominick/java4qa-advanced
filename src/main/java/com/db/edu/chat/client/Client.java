package com.db.edu.chat.client;


import java.io.IOException;
import com.db.edu.chat.connection.Connection;
import com.db.edu.chat.connection.ConsoleConnection;
import com.db.edu.chat.connection.RealClientConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private Client() {
    }

    public static void main(String... args) {
        Connection networkConnection = new RealClientConnection();
        Connection consoleConnection = new ConsoleConnection();

        if(!networkConnection.accept()){
            LOGGER.error("Network connection to server was not established. Shutting down.");
            return;
        }
        if(consoleConnection.accept()){
            LOGGER.error("Problems with the console. See console log for details.");
            return;
        }

        Thread thread = new Thread(new ClientThreadAction(networkConnection,consoleConnection)) ;
        thread.start();

        while(true) {
            try {
                networkConnection.write(consoleConnection.read());
            } catch (IOException e) {
                networkConnection.close();
                consoleConnection.close();
            }
        }
    }
}
