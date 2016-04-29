package com.db.edu.chat.server;

import java.io.IOException;
import java.net.ServerSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;



public class Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    @Value("${host}")
    String host;

    public static final int PORT = 4498;
    private  ServerSocket serverSocket;
    private ServerThreadAction threadAction;

    public void start() throws ServerError {
        try {
            serverSocket = new ServerSocket(PORT);
            threadAction = new ServerThreadAction(serverSocket);
            new Thread(threadAction).start();
        } catch (IOException e) {
            throw new ServerError(e);
        }
    }

    public void stop() throws ServerError {
        if(threadAction !=null)
            threadAction.requestStop();

        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e1) {
            LOGGER.warn("Server was interrupted: ",e1);
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new ServerError(e);
        }
    }

    public static void main(String... args) throws ServerError {
        new Server().start();
    }
}
