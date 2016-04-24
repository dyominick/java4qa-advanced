package com.db.edu.chat.client;


import java.io.IOException;
import com.db.edu.chat.common.Connection;


public class Client {
	private Client() {
	}

	public static void main(String... args) throws IOException {
		Connection con = new RealClientConnection();

		Thread thread = new Thread(new ClientThreadAction(con)) ;
        thread.start();

		
		while(true) {
			con.write(con.consoleRead());
		}
	}
}
