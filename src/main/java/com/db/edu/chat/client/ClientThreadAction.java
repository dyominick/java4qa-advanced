package com.db.edu.chat.client;

import com.db.edu.chat.connection.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class ClientThreadAction implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ClientThreadAction.class);
    private Connection con;
    public ClientThreadAction(Connection con){
        this.con=con;
    }
    @Override
    public void run() {
        while(true) {
            try {
                String message = con.read();
                if(message == null) break;

                System.out.println(message);
            } catch (IOException e) {
                logger.error("IO exception: ",e);

            }
        }
    }
}
