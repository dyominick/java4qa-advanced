package com.db.edu.chat.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;

public interface BusinessLogic {
    int handle() throws IOException;
}
