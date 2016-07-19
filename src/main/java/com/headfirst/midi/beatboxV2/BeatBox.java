package com.headfirst.midi.beatboxv2;

import com.headfirst.midi.beatboxv2.chat.ChatClientThreaded;
import com.headfirst.midi.beatboxv2.chat.ChatMessage;
import com.headfirst.midi.beatboxv2.chat.ChatServer;
import com.headfirst.midi.beatboxv2.chat.Subscriber;
import com.headfirst.midi.beatboxv2.sound.Instruments;
import com.headfirst.midi.beatboxv2.sound.SimpleMidiPlayer;
import com.headfirst.midi.beatboxv2.sound.SongChannel;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tom on 5/25/2016.
 */
/*
Version 2
- adds simple socket communication
- run ChatServer to mediate comm between BeatBox instances

Features
- start multiple independent BeatBox (BB) GUI instances
- if chatserver is running, any BB can send messages that broadcasts to all
- BB message holds also BB pattern from the sender GUI
- clicking on message will load up BB pattern associated with the message
- BB pattern can be re-played

 */
public class BeatBox extends JPanel implements Subscriber {

    public static final int sequenceLength = 16;
    private final int note = 40;
    private final HashMap<String, List<Boolean>> messagesMap = new HashMap<>();
    private int[] guiInstrumentsIndexes =
            {3, 5, 10, 22, 28, 35, 40, 47, 57, 66, 69, 87, 91, 105, 114, 127};
    private SwitchBoard switchBoard;
    private SimpleMidiPlayer player;
    private ChatClientThreaded chat;
    private JTextArea outgoingMessages;
    private DefaultListModel<String> incomingListModel;
    private JList<String> incomingList;

    public BeatBox() {
        switchBoard = new SwitchBoard(sequenceLength, guiInstrumentsIndexes);
        makeGui();
        try {

            player = new SimpleMidiPlayer();
            chat = new ChatClientThreaded(
                    ChatServer.DEFAULT_IP, ChatServer.DEFAULT_PORT);
            chat.attachSubscriber(this);
            Thread backgroundListener = new Thread(chat);
            backgroundListener.start();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Chat connection unavailable");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BeatBox box = new BeatBox();

        JFrame frame = new JFrame(box.getClass().getSimpleName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(box);
        frame.pack();
        frame.setLocation(300, 250);
        frame.setVisible(true);
    }

    void makeGui() {
        JPanel controlsPane = new JPanel();
        controlsPane.setLayout(new BoxLayout(controlsPane, BoxLayout.PAGE_AXIS));

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(getSwitchBoardPane());
        add(getControlPane());
    }

    JPanel getControlPane() {
        JPanel controlPane = new JPanel();
        controlPane.setLayout(new BorderLayout());

        JPanel buttonBar = new JPanel();
        buttonBar.setLayout(new BoxLayout(buttonBar, BoxLayout.PAGE_AXIS));
        buttonBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 5));

        JButton start = new JButton("Start");
        start.addActionListener(new StartListener());
        JButton stop = new StopButton("Stop");
        JButton tempoUp = new JButton("Tempo up");
//        anonymous class replaced with lambda as inspection advised
        tempoUp.addActionListener(actionEvent -> {
            player.scaleTempo(1.03);
        });
        JButton tempoDown = new JButton("Tempo down");
        tempoDown.addActionListener(actionEvent -> {
            player.scaleTempo(0.97);
        });
        JButton clearBtn = new JButton("Clear all");
        clearBtn.addActionListener((actionEvent) -> switchBoard.resetBoard());
        JButton sendMsg = new JButton("Send it");
        sendMsg.addActionListener((actionEv) -> sendMsgToChatServer());

        buttonBar.add(start);
        buttonBar.add(Box.createRigidArea(new Dimension(0, 5)));
        buttonBar.add(stop);
        buttonBar.add(Box.createRigidArea(new Dimension(0, 5)));
        buttonBar.add(tempoUp);
        buttonBar.add(Box.createRigidArea(new Dimension(0, 5)));
        buttonBar.add(tempoDown);
        buttonBar.add(Box.createRigidArea(new Dimension(0, 5)));
        buttonBar.add(clearBtn);
        buttonBar.add(Box.createRigidArea(new Dimension(0, 5)));
        buttonBar.add(sendMsg);
        buttonBar.add(Box.createRigidArea(new Dimension(0, 5)));

        outgoingMessages = new JTextArea(5, 15);
        outgoingMessages.setLineWrap(true);

        JPanel textFields = new JPanel();
        textFields.setLayout(new BoxLayout(textFields, BoxLayout.PAGE_AXIS));
        textFields.add(outgoingMessages);
        textFields.add(Box.createRigidArea(new Dimension(0, 5)));
        textFields.add(getIncomingArea());
        textFields.setBorder(BorderFactory.createEmptyBorder(5, 7, 5, 7));

        controlPane.add(BorderLayout.WEST, buttonBar);
        controlPane.add(BorderLayout.SOUTH, textFields);

        return controlPane;
    }

    //    Sequence switchboard for available music instruments
    Container getSwitchBoardPane() {
        JCheckBox[][] board = switchBoard.getBoard();

        Box rows = Box.createVerticalBox();
        Instruments instruments = new Instruments();
        int nextIndex = 0;
        String name;

        for (int i = 0; i < guiInstrumentsIndexes.length; i++) {
            Box line = Box.createHorizontalBox();
            name = instruments.getByIndex(guiInstrumentsIndexes[nextIndex]);
            nextIndex++;
            line.add(new Label(name));

            for (int j = 0; j < sequenceLength; j++) {
                JCheckBox check = new JCheckBox();
                line.add(check);
                board[i][j] = check;
            }
            rows.add(line);
        }
        return rows;
    }

    private Container getIncomingArea() {
        incomingListModel = new DefaultListModel<>();

//        default list model is immutable
//        for mutable, pass in class that adheres to ListModel interface
        incomingList = new JList<>(incomingListModel);
        incomingList.setLayoutOrientation(JList.VERTICAL);
        incomingList.setSelectionModel(new DefaultListSelectionModel());
        incomingList.setVisibleRowCount(5);
        incomingList.addListSelectionListener(new CommentClickListener());

        JScrollPane scroller = new JScrollPane(incomingList);
        scroller.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scroller;
    }

    //    this method does not belong well to BeatBox class
    List<SongChannel> getSongChannels() {
        List<SongChannel> songChannels = new ArrayList<>();
        int instrumentId;
        JCheckBox[][] board = switchBoard.getBoard();

        for (int i = 0; i < board.length; i++) {
            instrumentId = guiInstrumentsIndexes[i];
            SongChannel channel = new SongChannel(instrumentId, note);
            channel.setMidiChannel(i);

            for (int j = 0; j < sequenceLength; j++) {

                if (board[i][j].isSelected()) {
                    channel.addTick(j + 1);     // start from tick 1
                }
            }
            if (channel.getTicks().size() > 0) {
                songChannels.add(channel);
            }
        }
        return songChannels;
    }

    private void sendMsgToChatServer() {
        String msg = outgoingMessages.getText().trim();
        if (msg.length() == 0) {
            return;
        }
        outgoingMessages.setText("");
        if (chat != null) {
            chat.sendObject(
                    new ChatMessage(msg, switchBoard.getBoardAsBoolean()));
        }
    }

    @Override
    public void processUpdate(ChatMessage msg) {
        incomingListModel.addElement(msg.getComment());
        messagesMap.put(msg.getComment(), msg.getBeatBoxPattern());
    }

    class StartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.out.println("play song");
            if (player == null) {
                return;
            }
            player.playBeatBoxSequence(getSongChannels());
        }

    }

    //    just for the sake of practice, different implementation of stop button
    class StopButton extends JButton implements ActionListener {

        StopButton(String name) {
            super(name);
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.out.println("stop song");
            if (player == null) {
                return;
            }
            Sequencer seq = player.getSequencer();
            if (seq.isOpen()) {
                seq.stop();
            }
            player.closeSequencer();
        }

    }

    class CommentClickListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent event) {
//        true when user has not yet finished selecting option(s)
            if (!event.getValueIsAdjusting()) {
                List<Boolean> newPattern = messagesMap.getOrDefault(
                        incomingList.getSelectedValue().trim(), null);
                if (newPattern == null) {
                    System.out.println("Error finding BeatBox pattern");
                } else {
                    switchBoard.setBoardFromBoolean(newPattern);
                }
            }
        }
    }

//    private void saveSong() {
//        JFileChooser chooser = new JFileChooser();
//        int status = chooser.showSaveDialog(this);
//        if (status == JFileChooser.CANCEL_OPTION) {
//            return;
//        }
//
//        try {
//            Serialize.serialize(chooser.getSelectedFile(),
//                    switchBoard.getBoardAsBoolean().toArray());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void restoreSong() {
//        JFileChooser chooser = new JFileChooser();
//        int status = chooser.showOpenDialog(this);
//        if (status == JFileChooser.CANCEL_OPTION) {
//            return;
//        }
//
//        List<Object> objects;
//        try {
//            objects = Serialize.deserializeAll(
//                    chooser.getSelectedFile());
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        List<Boolean> newBoard = new ArrayList<>();
//        for (Object obj : objects) {
//            newBoard.add((Boolean) obj);
//        }
//        switchBoard.setBoardFromBoolean(newBoard);
//    }
}
