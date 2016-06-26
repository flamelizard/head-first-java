package com.headfirst.swing;

import com.headfirst.midi.basics.MidiUtils;
import com.sun.media.sound.SoftSynthesizer;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 5/25/2016.
 */
/*
Where to find best documentation on Swing?
Various ebooks do not cover all the essential details. Most complete is
official Swing documentation at Oracle. Pay attention to opening chapters
that cover Swing foundations and provide guidance on common issues.

Space around components - some layout managers can set it explicitly, other
relies on components e.g. setting empty border.

Component spacing
- create empty border on container .setBorder()
- set widget margin .setMargin(new Insets(...))

Alignment issues are often caused by different alignment setting. E.g.
JButton and JPanel are not consistent. Check out getAlignmentX etc.

Sometimes, it may be necessary to explicitly set components preferred size to
 hint layout mgr on proper sizing. Methods "setPreferredSize" etc.

 Empty panel added to container can help to position other components to the top
 */
public class BeatBox extends JPanel {

    private int[] instrumentIndexes =
            {3, 5, 10, 22, 40, 47, 57, 66, 69, 78, 91, 105, 114, 127, 184, 209};
    private final int numInstruments = 16;
    private final int sequenceLength = 16;
    private JCheckBox[][] switchBoard;

    public BeatBox() {
        makeGui();
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
//        controlsPane.setLayout(new BorderLayout());
        controlsPane.setBackground(Color.cyan);

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
        JButton tempoDown = new JButton("Tempo down");

        controls.add(start);
        controls.add(Box.createRigidArea(new Dimension(0, 5)));
        controls.add(stop);
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
            name = instruments.getByIndex(instrumentIndexes[nextIndex]);
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

    class Instruments {
        Instrument[] all;

        Instruments() {
            SoftSynthesizer synth = new SoftSynthesizer();
            all = synth.getAvailableInstruments();
        }

        String getByIndex(int index) {
            return all[index].getName();
        }

        int getByName(String name) {
            for (int i = 0; i < all.length; i++) {
                if (all[i].getName().toLowerCase().equals(name)) {
                    return i;
                }
            }
            return -1;
        }

        void printInstrumentsMap() {
            for (int i = 0; i < all.length; i++) {
                System.out.println(String.format("%d -> %s", i, all[i]));
            }
        }
    }


    int[][] readSwitchBoard() {
        int[][] instrumentToTicks = new int[numInstruments][sequenceLength];

        for (int i = 0; i < numInstruments; i++) {
            byte index = 0;

            for (int j = 0; j < sequenceLength; j++) {
                if (switchBoard[i][j].isSelected()) {
                    instrumentToTicks[i][index++] = j;
                }
            }
        }
        return instrumentToTicks;
    }

    class StartListener implements ActionListener {

        private PlayMidi player;

        StartListener() {
            try {
                player = new PlayMidi();
            } catch (MidiUnavailableException e) {
                System.out.println("MIDI API not available");
            }
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.out.println("play song");
            if (player == null) {
                return;
            }

            try {
                player.playBeatBoxSequence(readSwitchBoard());
            } catch (MidiUnavailableException e) {
                System.out.println("MIDI API not available");
            } catch (InvalidMidiDataException e) {
                System.out.println("MIDI sequence data invalid");
            }
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
        }
    }

    class PlayMidi {
        private Sequencer sequencer;
        final int channel = 1;
        final int note = 42;
        final int velocity = 100;
        final int tickStep = 10;
        private int[][] instrumentToTicks;

        PlayMidi() throws MidiUnavailableException {
            sequencer = MidiSystem.getSequencer();
        }

        public void playBeatBoxSequence(int[][] instrumentToTicks)
                throws InvalidMidiDataException, MidiUnavailableException {

            this.instrumentToTicks = instrumentToTicks;
            Sequence sequence = MidiUtils.makeSequence(convertToEvents());

            openSequencer();
            sequencer.setSequence(sequence);
            sequencer.start();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            closeSequencer();

        }

        private void openSequencer() throws MidiUnavailableException {
            if (sequencer != null) {
                sequencer.open();
            }
        }

        private void closeSequencer() {
            if (sequencer != null) {
                sequencer.close();
            }
        }

        private MidiEvent[] convertToEvents()
                throws InvalidMidiDataException {

            List<MidiEvent> events = new ArrayList<>();
            int tick;

            for (int i = 0; i < instrumentToTicks.length; i++) {
                tick = 0;
                events.add(MidiUtils.makeEventInstrumentChange(i, tick));

                for (int j = 0; j < instrumentToTicks[0].length; j++) {
//                    time slot when the instrument plays
                    tick = instrumentToTicks[i][j] * tickStep;
                    events.add(MidiUtils.makeEvent(ShortMessage.NOTE_ON,
                            channel, note, velocity, tick));
                }
            }
            return events.toArray(new MidiEvent[]{});
        }

        private void printInstrumentToTicks() {
            for (int i = 0; i < instrumentToTicks.length; i++) {
                StringBuilder log = new StringBuilder("[" + i + "] ");
                for (int j = 0; j < instrumentToTicks[0].length; j++) {
                    log.append(j).append(",");
                }
                System.out.println(log);
            }
        }
    }

    List<Instrument> getInstrumentsByIndex(byte[] indexes) {
        SoftSynthesizer synth = new SoftSynthesizer();
        java.util.List<Instrument> all = new ArrayList<>();

        Instrument[] instruments = synth.getAvailableInstruments();
        for (byte index : indexes) {
            all.add(instruments[index]);
        }
        return all;
    }
}
