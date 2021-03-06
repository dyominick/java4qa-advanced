
package com.db.edu.chat.connection;

import java.io.IOException;

public interface Connection {

    String read() throws IOException;

    void write (String message) throws IOException;

    void close();

    void accept() throws IOException;

}
