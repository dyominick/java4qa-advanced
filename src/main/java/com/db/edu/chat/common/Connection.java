package com.db.edu.chat.common;

import java.io.IOException;

/**
 * Created by Student on 22.04.2016.
 */
public interface Connection {
    public String read() throws IOException;
    public void write (String message) throws IOException;
    default void close() throws IOException {};
    String consoleRead() throws IOException;
}
