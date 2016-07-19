package com.headfirst.sockets.adviceguy;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Tom on 6/27/2016.
 */
public class TheAdviceGuyServer {
    public static final String LOCALHOST = "127.0.0.1";
    public static final short TCP_PORT = 5000;

    private ArrayList<String> advices = new ArrayList<>(
            Arrays.asList(
                    "Change socks regularly", "Start day with a smile",
                    "Drink with friends", "Don't forget deo stick")
    );

    public TheAdviceGuyServer() {
    }

    public static void main(String[] args) {
        TheAdviceGuyServer server = new TheAdviceGuyServer();
        server.serveAdvices();
    }

    public void serveAdvices() {
        try {
            ServerSocket serverSock = new ServerSocket(TCP_PORT);
            PrintWriter writer;
            while (true) {
                Socket sock = serverSock.accept();
                writer = new PrintWriter(sock.getOutputStream());
//                flush all advices to a client socket
                writer.write(String.join("\n", advices));
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
