package com.db.edu.chat.connection;

import java.io.IOException;

public interface Connection {
    String read() throws IOException;
    void write (String message) throws IOException;
    default void close() throws IOException {}
    default String consoleRead() throws IOException {
        return null;
    }
}
