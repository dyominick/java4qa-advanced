package com.db.edu.chat.server;


import static com.db.edu.chat.server.TestUtils.sleep;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import com.db.edu.chat.common.MyProperties;
import org.junit.Ignore;
import org.junit.Test;


public class ChatServerAdminTest {
	private Server testServer;
	
	@Test(timeout=3000)
	public void shouldListenPortWhenStarted() throws ServerError, IOException {
		testServer = new Server();
		testServer.start();
		sleep(300);
		
		try {
			new Socket(MyProperties.getHost(), Server.PORT);
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

		Socket testSocket = new Socket(MyProperties.getHost(), Server.PORT);
		testSocket.close();
		System.out.println("shouldReleasePortWhenStopped test finished");
	}
}
