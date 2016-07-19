package com.headfirst.sockets.adviceguy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Tom on 6/27/2016.
 */
public class TheAdviceGuyClient {
    Socket socket;
    BufferedReader reader;

    public TheAdviceGuyClient(String IPaddr, int port) throws IOException {
        if (port < 1023) {
            throw new IllegalArgumentException(
                    "Port number in well-known ports range");
        }
        socket = new Socket(IPaddr, port);
    }

    public TheAdviceGuyClient() throws IOException {
        this(TheAdviceGuyServer.LOCALHOST, TheAdviceGuyServer.TCP_PORT);
    }

    public static void main(String[] args) throws IOException {
        TheAdviceGuyClient chat = new TheAdviceGuyClient();
        chat.startInteractiveSession();
    }

    public void startInteractiveSession() {
        String prompt = "Press any key ['q' to quit]: ";
        String reply = this.waitForKeyPress(prompt);
        while (!reply.toLowerCase().equals("q")) {
            try {
                System.out.println("[advice] " + this.retrieveAdvice());
            } catch (IOException e) {
                e.printStackTrace();
                close();
                return;
            }
            reply = this.waitForKeyPress(prompt);
        }
        close();
    }

    private String retrieveAdvice() throws IOException {
        if (reader == null) {
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
        }
        return reader.readLine();
    }

    private String waitForKeyPress(String prompt) {
        System.out.print(prompt);
        BufferedReader bin = new BufferedReader(
                new InputStreamReader(System.in)
        );
        String reply = "";
        try {
            reply = bin.readLine();
        } catch (IOException e) {
        }
        System.out.println();
        return reply;
    }

    private void close() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
