package com.db.edu.chat.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class ConsoleConnection implements Connection{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleConnection.class);
    private BufferedReader consoleReader;
    private BufferedWriter consoleWriter;

    public ConsoleConnection() {
    }

    @Override
    public String read() throws IOException {
        return consoleReader.readLine();
    }

    @Override
    public void write(String message) throws IOException {
        consoleWriter.write(message);
        consoleWriter.newLine();
        consoleWriter.flush();
    }

    @Override
    public void close() {
        try {
            consoleReader.close();
            consoleWriter.close();
        }
        catch (IOException e) {
            LOGGER.error("IO Error: ", e);
        }
    }

    @Override
    public void accept() throws IOException{

        consoleReader = new BufferedReader(new InputStreamReader(System.in));
        consoleWriter = new BufferedWriter(new OutputStreamWriter(System.out));
    }
}
