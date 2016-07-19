package com.headfirst.midi.beatboxv2.chat;

import java.io.IOException;
import java.net.SocketException;

/**
 * Created by Tom on 7/19/2016.
 */
/*
Just for learning of inheritance, have simple chat client and extend it for
more advanced client
 */
public class ChatClientThreaded extends ChatClient implements Runnable {
    public ChatClientThreaded(String serverIP, int port) throws IOException {
        super(serverIP, port);
    }

    @Override
    public void run() {
        try {
            serverObjectListener();
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("[server listener error]");
            e.printStackTrace();
        }
    }

    private void serverObjectListener()
            throws ClassNotFoundException, IOException {
        Object o;
        while (true) {
            try {
                o = objReader.readObject();
            } catch (SocketException socketClosed) {
                break;
            }
            relayUpdateToSubscriber((ChatMessage) o);
        }
        System.out.println("[server closed]");
    }

    //    add object interested in receiving server messages
    public void attachSubscriber(Subscriber sub) {
        this.subscriber = sub;
    }

    private void relayUpdateToSubscriber(ChatMessage msg) {
        if (subscriber == null) {
            return;
        }
        subscriber.processUpdate(msg);
    }
}
