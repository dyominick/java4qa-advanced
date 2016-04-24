package com.db.edu.chat.server;

import com.db.edu.chat.common.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Student on 22.04.2016.
 */
public class RealConnection implements Connection {

    private static final Logger logger = LoggerFactory.getLogger(RealConnection.class);
    private ServerSocket serverSocket;
    private Socket clientSocket;
    public RealConnection(ServerSocket serverSocket) throws IOException {
        this.serverSocket=serverSocket;
        accept();
        logger.info("Client connected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
    }

    private void accept() throws IOException {
        clientSocket = serverSocket.accept();
    }
    @Override
    public String read() throws IOException {
        BufferedReader socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String message = socketReader.readLine();
        logger.info("Message from client "
                + clientSocket.getInetAddress() + ":"
                + clientSocket.getPort() + "> "
                + message);
        return  message;
    }

    @Override
    public void write (String message) throws IOException {
        if (clientSocket.isClosed())
            return;
        if (!clientSocket.isBound())
            return;
        if (!clientSocket.isConnected())
            return;
        logger.info("Writing message " + message + " to socket " + clientSocket);

        BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        socketWriter.write(message);
        socketWriter.newLine();
        socketWriter.flush();
    }

    @Override
    public void close() throws IOException {
        clientSocket.close();
    }

    @Override
    public String consoleRead() {
        return null;

    }

}
