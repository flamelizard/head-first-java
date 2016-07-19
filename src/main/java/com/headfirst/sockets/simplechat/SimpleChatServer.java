package com.headfirst.sockets.simplechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 6/30/2016.
 */

public class SimpleChatServer {

    private ServerSocket serverSocket;
    private List<Socket> clients = new ArrayList<>();

    public SimpleChatServer() throws IOException {
        bindPort();
    }

    public static void main(String[] args) {
        try {
            SimpleChatServer server = new SimpleChatServer();
            server.runDaemon();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void bindPort() throws IOException {
        serverSocket = new ServerSocket(
                SimpleChatClient.SERVER_PORT);
    }

    public void runDaemon() {
        System.out.println("[daemon]");
        Thread clientThread;
        while (true) {
            try {
                Socket sock = serverSocket.accept();
//                Predicate can be a lambda
                boolean isSocketSaved = clients.stream()
                        .anyMatch((sockX) -> sockX.equals(sock));
                if (!isSocketSaved) {
                    clients.add(sock);
                }
                System.out.println("[clients] " + clients);

                clientThread = new Thread(new ClientHandler(sock));
                clientThread.start();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private void removeClosedSockets() {
//        Collections variable grows and shrinks !! automatically
        byte removedCnt = 0;
        for (int i = 0; i < clients.size(); i++) {
            i -= removedCnt;
            if (clients.get(i).isClosed()) {
                clients.remove(i);
                removedCnt++;
            }
        }

    }

    //    method shared by client threads, only one thread can acquire object lock
    private synchronized void broadcastMessage(String msg) throws IOException {
        System.out.println("[broadcast] " + msg);
        PrintWriter writer;
        removeClosedSockets();
        for (Socket sock : clients) {
            writer = new PrintWriter(sock.getOutputStream());
            writer.println(msg);
            writer.flush();
        }
    }

    //    Thread's job class
    class ClientHandler implements Runnable {
        private BufferedReader reader;

        ClientHandler(Socket sock) throws IOException {
            this.reader = new BufferedReader(
                    new InputStreamReader(sock.getInputStream())
            );
        }

        public void run() {
            String line;
            while (true) {
                try {
//                    reader blocks until it gets precisely one line
                    line = reader.readLine();
                    broadcastMessage(line);
                    System.out.println("[line " + line);
                    Thread.sleep(1000);
                } catch (IOException | InterruptedException expectedOnExit) {
//                    expectedOnExit.printStackTrace();
                    return;
                }
            }
        }
    }
}
