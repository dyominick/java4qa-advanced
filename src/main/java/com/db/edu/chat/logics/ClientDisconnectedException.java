package com.db.edu.chat.logics;

/**
 * Created by Student on 27.04.2016.
 */
public class ClientDisconnectedException extends Exception {
    public ClientDisconnectedException() {
    }

    public ClientDisconnectedException(String message) {
        super(message);
    }

    public ClientDisconnectedException(Throwable cause) {
        super(cause);
    }
}
