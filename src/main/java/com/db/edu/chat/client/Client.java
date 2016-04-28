package com.db.edu.chat.client;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        try {
            networkConnection.accept();
        }
        catch (IOException e){
                LOGGER.error("Network connection to server was not established: ", e);
                return;
            }
        try {
            consoleConnection.accept();
        }
        catch(IOException e){
                LOGGER.error("Problems with the console: ", e);
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
