package com.db.edu.chat.connection;

import com.db.edu.chat.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import java.io.*;
import java.net.Socket;


public class RealClientConnection implements Connection {
    private static final Logger LOGGER = LoggerFactory.getLogger(RealClientConnection.class);
    private Socket socket;
    private BufferedWriter socketWriter;
    private BufferedReader socketReader;

    @Value("${host}")
    private String host;

    @Override
    public String read() throws IOException {
        return  socketReader.readLine();
    }

    @Override
    public void write(String message) throws IOException {
        socketWriter.write(message);
        socketWriter.newLine();
        socketWriter.flush();
    }

    @Override
    public void close()  {
        try {
            socket.close();
            socketReader.close();
            socketWriter.close();
        }
        catch (IOException e) {
            LOGGER.error("IO Error: ", e);
        }

    }

    @Override
    public void accept() throws IOException{
        socket = new Socket(host, Server.PORT);
        socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
}
