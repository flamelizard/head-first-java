package com.headfirst.sockets.simplechat;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Tom on 6/28/2016.
 */
public class SimpleChatClient extends JPanel {
    public static final String SERVER_IP = "127.0.0.1";
    public static final short SERVER_PORT = 5000;
    private JTextField input;
    private PrintWriter writer;
    private BufferedReader reader;
    private JTextArea chatWindow;

    public SimpleChatClient() {
        makeGUI();
        try {
            setUpConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Thread threadListener = new Thread(new ServerMessageListener());
        threadListener.start();

        JFrame frame = new JFrame(this.getClass().getSimpleName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(500, 300);
        frame.setContentPane(this);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SimpleChatClient chat = new SimpleChatClient();
    }

    private void makeGUI() {
        input = new JTextField(15);
        JButton sendBtn = new JButton("send");
        sendBtn.addActionListener(
                (action) -> sendUserMessage());

        JPanel inputPane = new JPanel();
        inputPane.add(input);
        inputPane.add(sendBtn);

        chatWindow = new JTextArea(10, 15);
        JScrollPane chatScroller = new JScrollPane(chatWindow);
        chatScroller.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatScroller.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(inputPane);
        add(chatScroller);
    }

    private void setUpConnection() throws IOException {
        Socket sock = new Socket(SERVER_IP, SERVER_PORT);
        writer = new PrintWriter(sock.getOutputStream());
        reader = new BufferedReader(
                new InputStreamReader(sock.getInputStream())
        );
    }

    private void sendUserMessage() {
//        System.out.println("[sending] " + writer);
        writer.println(input.getText().trim());
        writer.flush();
        input.setText("");
        input.requestFocus();
    }

    //    thread's job
    class ServerMessageListener implements Runnable {
        public void run() {
            System.out.println("[thread job] " +
                    Thread.currentThread().getName());
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    chatWindow.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("[thread exits]");
        }
    }
}
