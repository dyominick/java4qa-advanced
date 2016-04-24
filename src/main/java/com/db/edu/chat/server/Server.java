package com.db.edu.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Collection;

import com.db.edu.chat.common.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
	private static final Logger logger = LoggerFactory.getLogger(Server.class);
	public static final int PORT = 4498;
	
	private final Collection<Connection> connections = new java.util.concurrent.CopyOnWriteArrayList<>();
	private volatile ServerSocket serverSocket;


	private Thread connectionEventLoop = new Thread() {
		@Override
		public void run() {
			while(!isInterrupted()) {

				try {
					Connection clientConnection = new RealConnection(serverSocket);
					connections.add(clientConnection);

					Thread clientConnectionHandler = new Thread(
						new ClientConnectionHandler(
								clientConnection,
								connections,
							new ChatBusinessLogic(clientConnection, connections)
						)
					);
					clientConnectionHandler.setDaemon(true);
					clientConnectionHandler.start();
				} catch (SocketException e) {
					logger.debug("Intentionally closed socket: time to stop",e);
					break;
				} catch (IOException e) {
					logger.error("Network error", e);
					break;
				}
			}
		}
	};

	public Server() throws IOException {

	}

	public void start() throws ServerError {
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			throw new ServerError(e);
		}
		connectionEventLoop.start();
	}
	
	public void stop() throws ServerError {
		connectionEventLoop.interrupt();
		
		try { Thread.sleep(1000); } catch (InterruptedException e1) { } 
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			throw new ServerError(e);
		}
	}
	
	public static void main(String... args) throws ServerError {
		try {
			new Server().start();
		} catch (IOException e) {
			logger.error("Couldn't initialize properties: ",e);
		}
	}
}
