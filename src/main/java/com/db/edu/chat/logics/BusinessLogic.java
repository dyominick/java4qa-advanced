package com.db.edu.chat.logics;

public interface BusinessLogic {
    void handle() throws ClientDisconnectedException, FailedConnectionException;
}
