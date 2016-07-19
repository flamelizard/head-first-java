package com.headfirst.midi.beatboxv2.chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom on 7/4/2016.
 */
/*
ChatServer binds to the port and listens for client connection.
Each connection is saved and any client message broadcasts to all clients.
 */
public class ChatServer {
    public static int DEFAULT_PORT = 5000;
    public static String DEFAULT_IP = "127.0.0.1";
    private ServerSocket serverSock;
    //    object writer gives more flexibility than string writer
    private Map<Integer, ObjectOutputStream> clients = new HashMap<>();

    public ChatServer(int port) throws IOException {
        serverSock = new ServerSocket(port);
    }

    public static void main(String[] args) {
        try {
            ChatServer server = new ChatServer(ChatServer.DEFAULT_PORT);
            server.runDaemon();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runDaemon() {
        System.out.println("[daemon]");
        Socket clientSock;
        ObjectOutputStream writer;
        while (true) {
            try {
                clientSock = serverSock.accept();
                writer = new ObjectOutputStream(clientSock.getOutputStream());
                clients.put(clientSock.getPort(), writer);

                Thread t = new Thread(new ClientHandler(clientSock));
                t.start();
            } catch (IOException e) {
//                do not fail, single client failure ok
                e.printStackTrace();
            }
        }
    }

    private synchronized void broadcastObject(Object o) {
        for (ObjectOutputStream os : clients.values()) {
            try {
                os.writeObject(o);
                os.flush();
            } catch (IOException e) {
                System.out.println("Error writing to a client");
                e.printStackTrace();
            }
        }
    }

    private synchronized void removeClientOnPort(Integer port) {
        clients.remove(port);
    }

    class ClientHandler implements Runnable {
        private final Integer PORT;
        private ObjectInputStream oReader;

        ClientHandler(Socket sock) throws IOException {
            PORT = sock.getPort();
            oReader = new ObjectInputStream(sock.getInputStream()
            );
        }

        @Override
        public void run() {
            listenForObjects();
            removeClientOnPort(PORT);
            System.out.println("[quit client handler thread]");
        }

        private void listenForObjects() {
            Object o;
            while (true) {
                try {
//                    blocks till object available
                    o = oReader.readObject();
                    System.out.println("[read obj] " + o);
                    broadcastObject(o);
                } catch (SocketException socketClosed) {
                    return;
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}
