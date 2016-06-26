package com.headfirst.midi.basics;

import com.sun.media.sound.SoftSynthesizer;

import javax.sound.midi.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Tom on 4/24/2016.
 */

/*
Condensed basics on generating music through Java MIDI API
 */
public class MusicPlayer {
    private Sequencer player;

    public MusicPlayer() {
        try {
            player = MidiSystem.getSequencer();
            /*
            Close opened sequencer at the end !!
            The process will not finish otherwise. Setting sequencer var
            (reference) to null does NOT help to quit properly.
             */
            player.open();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MusicPlayer music = new MusicPlayer();
//        music.playNote();
//        try {
//            music.playManyInstruments();
//        }catch (InvalidMidiDataException e) {
//            e.printStackTrace();
//        }
        music.playInstrumentOnUserInput();
        music.player.stop();
        music.player.close();

    }

    void playNote() {
        try {
            Sequence seq = new Sequence(Sequence.PPQ, 4);
            Track track = seq.createTrack();

            ShortMessage a = new ShortMessage();
            a.setMessage(ShortMessage.NOTE_ON, 1, 44, 100);
            MidiEvent noteOn = new MidiEvent(a, 1);
            track.add(noteOn);

            ShortMessage b = new ShortMessage();
            b.setMessage(ShortMessage.NOTE_OFF, 1, 44, 100);
            MidiEvent noteOff = new MidiEvent(b, 16);
            track.add(noteOff);

            ShortMessage c = new ShortMessage();
            c.setMessage(ShortMessage.PROGRAM_CHANGE, 1, 70, 0);
            MidiEvent newInstrument = new MidiEvent(c, 0);
            track.add(newInstrument);

            player.setSequence(seq);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        player.start();
    }

    void playManyInstruments() throws InvalidMidiDataException {
        Sequence seq = new Sequence(Sequence.PPQ, 4);
        Track track = seq.createTrack();
        MidiEvent event = null;
        int ticks = 1;  // or beat, it does not equal to a time unit like sec

        for (int instrument = 0; instrument < 100; instrument = instrument +
                10) {
            event = makeMidiEvent(ShortMessage.PROGRAM_CHANGE, 1, instrument, 0,
                    ticks);
            track.add(event);

            event = makeMidiEvent(ShortMessage.NOTE_ON, 1, 44, 100, ticks + 1);
            track.add(event);

            event = makeMidiEvent(ShortMessage.NOTE_OFF, 1, 44, 100, ticks
                    + 20);
            track.add(event);
            ticks += 10;

        }
        player.setSequence(seq);
        player.start();
    }

    void playInstrumentOnUserInput() {
        String instr = "1";
        String note = "44";

        while (true) {
            try {
                System.out.println("['q' to quit]");
                instr = getUserInput("Enter instrument number: ");
                note = getUserInput("Enter note: ");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (instr.toLowerCase().equals("q") || note.toLowerCase().equals
                    ("q")) {
                System.out.println("Quit...");
                return;
            }
            String msg = String.format("Playing note <%s> on instrument <%s>",
                    note, getInstrumentNameByIndex(Integer.parseInt(instr)));
            System.out.println(msg);
            try {
                playSingleInstrument(Integer.parseInt(instr), Integer
                        .parseInt(note));
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
            System.out.println();

        }
    }

    void playSingleInstrument(int num, int note) throws InvalidMidiDataException {
        Sequence seq = new Sequence(Sequence.PPQ, 4);
        Track track = seq.createTrack();
        MidiEvent event;
        int timeLine = 1;

        event = makeMidiEvent(ShortMessage.PROGRAM_CHANGE, 1, num, 0, timeLine);
        track.add(event);

        event = makeMidiEvent(ShortMessage.NOTE_ON, 1, note, 100, timeLine + 1);
        track.add(event);

        event = makeMidiEvent(ShortMessage.NOTE_OFF, 1, note, 100, timeLine + 10);
        track.add(event);

        player.setSequence(seq);
        player.start();
    }

    //    helper func to properly create MIDI event
    MidiEvent makeMidiEvent
    (int type, int channel, int note, int velocity, int when) {
        ShortMessage m = new ShortMessage();
        try {
            m.setMessage(type, channel, note, velocity);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
            return null;
        }
        return new MidiEvent(m, when);
    }

    String getUserInput(String msg) throws IOException {
        BufferedReader keyboard = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println(msg);
        return keyboard.readLine();
    }

    String getInstrumentNameByIndex(int index) {
        Synthesizer synt = new SoftSynthesizer();
        index = index == 0 ? 0 : index - 1;
        Instrument instr = synt.getAvailableInstruments()[index];
        return instr.getName().trim();
    }
}
