package com.db.edu.chat.logics;

import com.db.edu.chat.connection.Connection;

public interface BusinessLogic {
    void handle(Connection incoming) throws ClientDisconnectedException, FailedConnectionException;
}
