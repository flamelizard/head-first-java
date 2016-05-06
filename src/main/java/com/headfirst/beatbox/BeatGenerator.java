package com.headfirst.beatbox;

import javax.sound.midi.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.headfirst.beatbox.MidiUtils.makeEvent;

/**
 * Created by Tom on 5/2/2016.
 */
public class BeatGenerator {
    private Sequencer sequencer;

    public BeatGenerator() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
        }catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InvalidMidiDataException {
        BeatGenerator player = new BeatGenerator();
        player.play();
    }

    public void closeSequencer() {
        sequencer.close();
    }

//    void play2() throws InvalidMidiDataException{
//        Sequence sequence = new Sequence(Sequence.PPQ, 4);
//        Track track = sequence.createTrack();
//        MidiEvent ev = makeEvent(
//                ShortMessage.NOTE_ON, 1, 44, 100, 1);
//        track.add(ev);
//        ev = makeEvent(
//                ShortMessage.NOTE_OFF, 1, 44, 100, 20);
//        track.add(ev);
//        sequencer.setSequence(sequence);
//        sequencer.start();
//
//        try {
//            System.out.println("in");
//            Thread.sleep(2000);
//            System.out.println("out");
//        }catch (InterruptedException e) {}
//        closeSequencer();
//    }

    public void play() throws InvalidMidiDataException {
        int[] ctrlEvents = {ShortMessage.CONTROL_CHANGE};
        sequencer.addControllerEventListener(new NoteOnListener(), ctrlEvents);

        Sequence sequence = makeTestSequence();
        sequencer.setSequence(sequence);
        sequencer.setTempoInBPM(220);
        sequencer.start();

//        no sound played if app finishes soon !!
        waitForKeyPress();
        closeSequencer();
    }

    public Sequence makeTestSequence() throws InvalidMidiDataException{
        MidiEvent event;
        int note = 44;
        int velocity = 100;
        final int ctrlEventId = 127;
        final int channel = 1;
//        default instrument is piano

        Sequence sequence = new Sequence(Sequence.PPQ, 4);
        Track track = sequence.createTrack();

        for (int tick = 1; tick < 30; tick += 5) {
            event = makeEvent(
                    ShortMessage.NOTE_ON, channel, note, velocity, tick);
            track.add(event);

//            inject ControllerEvent to indicate NOTE_ON event
            event = makeEvent(
                    ShortMessage.CONTROL_CHANGE, channel, ctrlEventId, 0, tick);
            track.add(event);

            event = makeEvent(
                    ShortMessage.NOTE_OFF, channel, note, velocity, tick + 3);
            track.add(event);
        }
        return sequence;
    }

    class NoteOnListener implements ControllerEventListener {

        @Override
        public void controlChange(ShortMessage shortMessage) {
            System.out.println("echo " + shortMessage);
        }
    }

    void waitForKeyPress() {
        BufferedReader keyboard = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println("Press any key...");
        try {
            keyboard.readLine();
        }catch (IOException e) {}
    }

}
