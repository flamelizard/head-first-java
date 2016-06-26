package com.headfirst.midi.beatbox;

import com.headfirst.quizcards.Serialize;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 5/25/2016.
 */
/*
Status: done
 */
public class BeatBox extends JPanel {

    public static final int numInstruments = 16;
    public static final int sequenceLength = 16;

    private final int note = 40;
    private int[] guiInstrumentsIndexes =
            {3, 5, 10, 22, 28, 35, 40, 47, 57, 66, 69, 87, 91, 105, 114, 127};
    private JCheckBox[][] switchBoard;
    private SimpleMidiPlayer player;

    public BeatBox() {
        makeGui();
        try {
            player = new SimpleMidiPlayer();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BeatBox box = new BeatBox();

        JFrame frame = new JFrame(box.getClass().getName());
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
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));
        controls.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 5));

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
        clearBtn.addActionListener((actionEvent) -> resetSwitchBoard());
        JButton save = new JButton("Save pattern");
        save.addActionListener((action) -> saveSong());
        JButton restore = new JButton("Restore");
        restore.addActionListener((action) -> restoreSong());

        controls.add(start);
        controls.add(Box.createRigidArea(new Dimension(0, 5)));
        controls.add(stop);
        controls.add(Box.createRigidArea(new Dimension(0, 5)));
        controls.add(tempoUp);
        controls.add(Box.createRigidArea(new Dimension(0, 5)));
        controls.add(tempoDown);
        controls.add(Box.createRigidArea(new Dimension(0, 5)));
        controls.add(clearBtn);
        controls.add(Box.createRigidArea(new Dimension(0, 5)));
        controls.add(save);
        controls.add(Box.createRigidArea(new Dimension(0, 5)));
        controls.add(restore);
        controls.add(Box.createRigidArea(new Dimension(0, 5)));

        pane.add(controls);
//        empty panel to move buttons to the top
        pane.add(new JPanel());

        return pane;
    }

    //    Sequence switchboard for available music instruments
    Container getSwitchBoardPane() {
        switchBoard = new JCheckBox[numInstruments][sequenceLength];
        Box rows = Box.createVerticalBox();
        Instruments instruments = new Instruments();
        int nextIndex = 0;
        String name;

        for (int i = 0; i < numInstruments; i++) {
            Box line = Box.createHorizontalBox();
            name = instruments.getByIndex(guiInstrumentsIndexes[nextIndex]);
            nextIndex++;
            line.add(new Label(name));

            for (int j = 0; j < sequenceLength; j++) {
                JCheckBox check = new JCheckBox();
                line.add(check);
                switchBoard[i][j] = check;
            }
            rows.add(line);
        }
        return rows;
    }

    List<SongChannel> readSwitchBoard() {
        List<SongChannel> songChannels = new ArrayList<>();
        int instrumentId;
        int tickOffset = 3;

        for (int i = 0; i < numInstruments; i++) {

            instrumentId = guiInstrumentsIndexes[i];
            SongChannel channel = new SongChannel(instrumentId, note);
            channel.setMidiChannel(i);

            for (int j = 0; j < sequenceLength; j++) {

                if (switchBoard[i][j].isSelected()) {
                    channel.addTick(j + 1);     // start from tick 1
                }
            }
            if (channel.getTicks().size() > 0) {
//                channel.offsetTicks(tickOffset);
                songChannels.add(channel);
            }
        }
        return songChannels;
    }

    class StartListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.out.println("play song");
            if (player == null) {
                return;
            }
            player.playBeatBoxSequence(readSwitchBoard());
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

    private void resetSwitchBoard() {
        for (int i = 0; i < numInstruments; i++) {
            for (int j = 0; j < sequenceLength; j++) {
                switchBoard[i][j].setSelected(false);
            }
        }
    }

    private void saveSong() {
        JFileChooser chooser = new JFileChooser();
        int status = chooser.showSaveDialog(this);
        if (status == JFileChooser.CANCEL_OPTION) {
            return;
        }

        try {
            Serialize.serialize(
                    chooser.getSelectedFile(), getSwitchBoardAsBooleanArr().toArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void restoreSong() {
        JFileChooser chooser = new JFileChooser();
        int status = chooser.showOpenDialog(this);
        if (status == JFileChooser.CANCEL_OPTION) {
            return;
        }

        List<Object> objects;
        try {
            objects = Serialize.deserializeAll(
                    chooser.getSelectedFile());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        List<Boolean> board = new ArrayList<>();
        for (Object obj : objects) {
            board.add((Boolean) obj);
        }
        setSwitchBoardFromBooleanArr(board);
    }

    private List<Boolean> getSwitchBoardAsBooleanArr() {
        List<Boolean> board = new ArrayList<>();
        for (int i = 0; i < switchBoard.length; i++) {
            for (int j = 0; j < switchBoard[0].length; j++) {
                if (switchBoard[i][j].isSelected()) {
                    board.add(true);
                } else {
                    board.add(false);
                }
            }
        }
        return board;
    }

    private void setSwitchBoardFromBooleanArr(List<Boolean> board) {
        for (int i = 0; i < board.size(); i += sequenceLength) {
            for (int j = 0; j < sequenceLength; j++) {
                switchBoard[i / sequenceLength][j].setSelected(board.get(i + j));
            }
        }
    }
}
