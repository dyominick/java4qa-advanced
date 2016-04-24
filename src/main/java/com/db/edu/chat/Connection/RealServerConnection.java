package com.db.edu.chat.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class RealServerConnection implements Connection {

    private static final Logger logger = LoggerFactory.getLogger(RealServerConnection.class);
    private ServerSocket serverSocket;
    private Socket clientSocket;
    public RealServerConnection(ServerSocket serverSocket) throws IOException {
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
