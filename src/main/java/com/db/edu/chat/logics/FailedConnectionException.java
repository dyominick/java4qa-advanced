package com.db.edu.chat.logics;

/**
 * Created by Student on 27.04.2016.
 */
public class FailedConnectionException extends Exception {
    public FailedConnectionException() {
    }

    public FailedConnectionException(String message) {
        super(message);
    }

    public FailedConnectionException(Throwable cause) {
        super(cause);
    }
}
