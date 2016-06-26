package com.headfirst.midi.basics;

import com.headfirst.swing.BeatBoxGui;

import javax.sound.midi.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Tom on 5/2/2016.
 */
/*
Play sample MIDI sequence
Accept listener for Note On event - ping listener at the time when a note is
played

Status: done
 */
public class BeatGenerator {
    private Sequencer sequencer;
    private int[] ctrlEvents = {127};

    public BeatGenerator() {
        try {
            sequencer = MidiSystem.getSequencer();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InvalidMidiDataException {
        BeatGenerator player = new BeatGenerator();
        player.playMidiSeq();
    }

    public void registerWidgetForCtrlEvents(BeatBoxGui.RectCanvas listener) {
        sequencer.addControllerEventListener(listener, ctrlEvents);
    }

    void openSequencer() {
        try {
            sequencer.open();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void closeSequencer() {
        sequencer.close();
    }

    public void playMidiSeq() throws InvalidMidiDataException {
        Sequence sequence = makeTestSequence();

        openSequencer();
        sequencer.setSequence(sequence);
        sequencer.setTempoInBPM(180);
        sequencer.start();

//        no sound played if app finishes soon !!
//        waitForKeyPress();
//        try {
//            Thread.sleep(50);
//        } catch (InterruptedException e) {}
//        closeSequencer();
    }

    Sequence makeTestSequence() throws InvalidMidiDataException {
        MidiEvent event;
        int note = 44;
        int velocity = 100;
        final int channel = 1;
//        default instrument is piano

        Sequence sequence = new Sequence(Sequence.PPQ, 4);
        Track track = sequence.createTrack();

        for (int tick = 1; tick < 30; tick += 5) {
            event = MidiUtils.makeEvent(
                    ShortMessage.NOTE_ON, channel, note, velocity, tick);
            track.add(event);

//            inject event ControllerEvent to indicate NOTE_ON event
            event = MidiUtils.makeEvent(
                    ShortMessage.CONTROL_CHANGE, channel, ctrlEvents[0], 0,
                    tick);
            track.add(event);

            event = MidiUtils.makeEvent(
                    ShortMessage.NOTE_OFF, channel, note, velocity, tick + 3);
            track.add(event);
        }
        return sequence;
    }

    void waitForKeyPress() {
        BufferedReader keyboard = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println("Press any key...");
        try {
            keyboard.readLine();
        } catch (IOException e) {
        }
    }

}
