package com.db.edu.chat.server;

import static org.junit.Assume.assumeNotNull;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.Socket;

public class JUnitLoadTest {
    private static final Logger logger = LoggerFactory.getLogger(JUnitLoadTest.class);

    private IOException gotException = null;

    @Value("${host}")
    String host;

    Server ser;
    Socket readerClientSocket;
    @Before
    public void setUp() throws ServerError, IOException {
        ser = new Server();
        ser.start();
    }
    @Test(timeout = 1000)
    public void shouldGetMessageBackWhenSendMessage() throws IOException, InterruptedException, ServerError {

        final String sentMessage = Thread.currentThread().getName() + ";seed:" + Math.random();
        logger.debug("Sending message: " + sentMessage);

        Socket readerClientSocket = null;
        try {
            readerClientSocket = new Socket(host, Server.PORT);
        } catch (IOException e) {
            logger.error("Can't connect to server: ", e);
        }
        assumeNotNull(readerClientSocket);

        final BufferedReader readerClientSocketReader
                = new BufferedReader(new InputStreamReader(readerClientSocket.getInputStream()));

        Thread readerClient = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String gotMessage;

                    do {
                        gotMessage = readerClientSocketReader.readLine();
                        logger.debug("Got msg: " + gotMessage);
                    } while (!sentMessage.equals(gotMessage));

                } catch (IOException e) {
                    gotException = e;
                }
            }
        });
        readerClient.start();

        final Socket writerClientSocket = new Socket(host, Server.PORT);
        final BufferedWriter writerClientSocketWriter = new BufferedWriter(new OutputStreamWriter(writerClientSocket.getOutputStream()));
        socketWrite(writerClientSocketWriter, sentMessage);

        readerClient.join();
        if(gotException != null) throw gotException;

    }


    private static void sleep(int seconds) {
        try { Thread.sleep(1000*seconds); } catch (InterruptedException e) { }
    }

    private static void socketWrite(BufferedWriter socketWriter, String text) throws IOException {
        socketWriter.write(text);
        socketWriter.newLine();
        socketWriter.flush();
    }
    @After
    public void cleanUp() throws ServerError, IOException {

        ser.stop();
        if (readerClientSocket!=null)
        readerClientSocket.close();
    }
}
