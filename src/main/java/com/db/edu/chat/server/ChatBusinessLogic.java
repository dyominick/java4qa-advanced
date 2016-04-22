package com.db.edu.chat.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Collection;

public class ChatBusinessLogic implements BusinessLogic {
    private static final Logger logger = LoggerFactory.getLogger(ChatBusinessLogic.class);

    private Socket inSocket;
    private Collection<Socket> clientsSockets;

    public ChatBusinessLogic(Socket inSocket, Collection<Socket> clientsSockets) {
        this.inSocket = inSocket;
        this.clientsSockets = clientsSockets;
    }

    @Override
    public void handle() {
        BufferedReader socketReader = new BufferedReader(new InputStreamReader(inSocket.getInputStream()));
        String message = socketReader.readLine();
        if(message == null) break;

        logger.info("Message from client "
            + inSocket.getInetAddress() + ":"
            + inSocket.getPort() + "> "
            + message);

        for (Socket outSocket : clientsSockets) {
            try {
                if (outSocket.isClosed()) continue;
                if (!outSocket.isBound()) continue;
                if (!outSocket.isConnected()) continue;
                if (outSocket == this.inSocket) continue;
                logger.info("Writing message " + message + " to socket " + outSocket);

                BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(outSocket.getOutputStream()));
                socketWriter.write(message);
                socketWriter.newLine();
                socketWriter.flush();
            } catch (IOException e) {
                logger.error("Error writing message " + message + " to socket " + outSocket + ". Closing socket", e);
                try {
                    outSocket.close();
                } catch (IOException innerE) {
                    logger.error("Error closing socket ", innerE);
                }

                logger.error("Removing connection " + outSocket);
                clientsSockets.remove(outSocket);
            }
        }

    }
}
