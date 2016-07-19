package com.headfirst.midi.beatboxv2.sound;

import javax.sound.midi.*;
import java.util.Arrays;

/**
 * Created by Tom on 5/2/2016.
 */
/*
Set of helper methods for creating MIDI events

Alternative way to access MIDI channels
It is possible to acquire "channel" object from synthesizer and set
parameters directly on the channel. Then pass it to a receiver instance.
    "MidiSystem.getSynthesizer().getChannels() -> MidiChannel[]"

Midi 1.0 specification (General Midi)
Any sound generator (synthesizer) compliant with GM 1.0 must adhere to
specified parameters.

Support for 16 channels, each capable of playing different instrument. At least
128 instruments available as program change numbers, with percussion at channel
10.

Any-non percussion instrument will play note on "note on" event. However,
percussion is different and will play different kind of percussion on "note
on" event.
This explains why example in Head First Java can get away playing various
percussion instruments without actually calling "program change" event. No
idea how to make percussion to play a note.

https://en.wikipedia.org/wiki/General_MIDI
http://www.music-software-development.com/midi-tutorial.html
 */
public class MidiUtils {

    // channel < 16, 3rd and 4th params < 128
    public static MidiEvent makeEvent(
            int type, int channel, int note, int velocity, int when)
            throws InvalidMidiDataException {

        ShortMessage m = new ShortMessage();
        m.setMessage(type, channel, note, velocity);
        return new MidiEvent(m, when);
    }

    public static MidiEvent makeEvent(int type, SongChannel song, int when)
            throws InvalidMidiDataException {

        return makeEvent(type, song.getMidiChannel(), song.getNote(),
                song.getVelocity(), when);
    }

    public static MidiEvent makeEventNoteOn
            (int channel, int note, int velocity, int when)
            throws InvalidMidiDataException {

        return makeEvent(ShortMessage.NOTE_ON, channel, note, velocity, when);
    }

    //    set MIDI channel (0-15) to required instrument (usually 0-127)
    public static MidiEvent makeEventSetInstrumentOnChannel(
            int channel, int instrumentId, int tick)
            throws InvalidMidiDataException {

        int ignored = 0;
        return makeEvent(
                ShortMessage.PROGRAM_CHANGE, channel, instrumentId, ignored,
                tick);
    }

    public static Sequence makeSequence(MidiEvent[] events)
            throws InvalidMidiDataException {

        Sequence s = new Sequence(Sequence.PPQ, 4);
        Track t = s.createTrack();
        for (MidiEvent event : events) {
            t.add(event);
        }
        return s;
    }

    public static void printEvent(MidiEvent ev) {
        System.out.println("[Midi event]");
        System.out.println(Arrays.toString(ev.getMessage().getMessage()));
    }

}
