package com.db.edu.chat.client;

import com.db.edu.chat.common.Connection;
import com.db.edu.chat.server.Server;

import java.io.*;
import java.net.Socket;

/**
 * Created by Student on 22.04.2016.
 */
public class RealClientConnection implements Connection {
    final Socket socket;
    final BufferedWriter socketWriter;
    final BufferedReader socketReader;
    final BufferedReader consoleReader;
    public RealClientConnection() throws IOException {
        socket = new Socket(Server.HOST, Server.PORT);
        socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public String read() throws IOException {
        return  socketReader.readLine();
    }
    public String consoleRead () throws IOException {
        return consoleReader.readLine();
    }

    @Override
    public void write(String message) throws IOException {
        socketWriter.write(message);
        socketWriter.newLine();
        socketWriter.flush();
    }

    @Override
    public void close() throws IOException {

    }
}
