package com.db.edu.chat.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class RealServerConnection implements Connection {

    private static final Logger LOGGER = LoggerFactory.getLogger(RealServerConnection.class);
    private ServerSocket serverSocket;
    private Socket clientSocket;
    public RealServerConnection(ServerSocket serverSocket) {
        this.serverSocket=serverSocket;
    }

    @Override
    public boolean accept() {
        if(!serverSocket.isClosed()) {
            try {
                clientSocket = serverSocket.accept();
                LOGGER.info("Client connected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            } catch(SocketException e) {
                LOGGER.info("Server is stopped.");
                return false;
            } catch (IOException e) {
                LOGGER.error("Error while accepting incoming connection: ", e);
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public String read() throws IOException {
        if(null!=clientSocket&&!clientSocket.isClosed()) {
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message = socketReader.readLine();
            LOGGER.info("Message from client "
                    + clientSocket.getInetAddress() + ":"
                    + clientSocket.getPort() + "> "
                    + message);
            return message;
        }
        else {
            LOGGER.warn("Client socket is closed");
            return null;
        }
    }

    @Override
    public void write (String message) throws IOException {
        if (clientSocket.isClosed())
            return;
        if (!clientSocket.isBound())
            return;
        if (!clientSocket.isConnected())
            return;
        LOGGER.info("Writing message " + message + " to socket " + clientSocket);

        BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        socketWriter.write(message);
        socketWriter.newLine();
        socketWriter.flush();
    }

    @Override
    public void close()  {

        try {
            clientSocket.close();
        } catch (IOException e) {
            LOGGER.error("IO Error: ", e);
        }
    }

}
