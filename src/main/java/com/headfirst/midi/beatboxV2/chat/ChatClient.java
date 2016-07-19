package com.headfirst.midi.beatboxv2.chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Tom on 7/4/2016.
 */
public class ChatClient {
    private final String SERVER_IP;
    private final int TCP_PORT;
    protected ObjectInputStream objReader;
    protected Subscriber subscriber;
    private ObjectOutputStream objWriter;

    public ChatClient(String serverIP, int port) throws IOException {
        SERVER_IP = serverIP;
        TCP_PORT = port;
        setUpConnection();
    }

    private void setUpConnection() throws IOException {
        int timeout = 3000;
        Socket sock = new Socket();
//        cannot set timeout if passing connection info to Socket constructor
        sock.connect(new InetSocketAddress(SERVER_IP, TCP_PORT), timeout);
        objWriter = new ObjectOutputStream(sock.getOutputStream());
        objReader = new ObjectInputStream(sock.getInputStream());
    }

    public void sendObject(Object payload) {
        try {
            objWriter.writeObject(payload);
            objWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            objWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
