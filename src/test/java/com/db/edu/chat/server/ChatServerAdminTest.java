package com.db.edu.chat.server;


import static com.db.edu.chat.server.TestUtils.sleep;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;


public class ChatServerAdminTest {
	private Server testServer;
	@Value("${host}")
	String host;
	
	@Test(timeout=3000)
	public void shouldListenPortWhenStarted() throws ServerError, IOException {
		testServer = new Server();
		testServer.start();
		sleep(300);
		
		try {
			new Socket(host, Server.PORT);
		} finally {
			testServer.stop();			
		}
		System.out.println("shouldListenPortWhenStarted test finished");
	}

	@Test(expected=ConnectException.class, timeout=3000)
	public void shouldReleasePortWhenStopped() throws ServerError, IOException {
		testServer = new Server();
		testServer.start();
		sleep(300);
		testServer.stop();

		Socket testSocket = new Socket(host, Server.PORT);
		testSocket.close();
		System.out.println("shouldReleasePortWhenStopped test finished");
	}
}
