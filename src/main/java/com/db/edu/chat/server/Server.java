package com.db.edu.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
	private static final Logger logger = LoggerFactory.getLogger(Server.class);
	public static final int PORT = 4498;
	private volatile ServerSocket serverSocket;
	private ConnectionEvent conEvent;
	private Thread connectionEventExecutor;

	public void start() throws ServerError {
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			throw new ServerError(e);
		}
		conEvent = new ConnectionEvent(serverSocket);
		connectionEventExecutor = new Thread(conEvent);
		connectionEventExecutor.start();
	}
	
	public void stop() throws ServerError {
		if(conEvent!=null)
		    conEvent.requestStop();
		
		try {
			Thread.sleep(1000);
		}
		catch (InterruptedException e1) {
			logger.warn("Server was interrupted: ",e1);
		}
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			throw new ServerError(e);
		}
	}
	
	public static void main(String... args) throws ServerError {
		new Server().start();
	}
}
