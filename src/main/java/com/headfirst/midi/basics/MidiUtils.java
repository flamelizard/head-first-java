package com.headfirst.midi.basics;

import javax.sound.midi.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tom on 5/2/2016.
 */
public class MidiUtils {

    //    helper func to properly create MIDI event
    public static MidiEvent makeEvent
    (int type, int channel, int note, int velocity, int when)
            throws InvalidMidiDataException {

        ShortMessage m = new ShortMessage();
        m.setMessage(type, channel, note, velocity);
        return new MidiEvent(m, when);
    }

    //    tick refer to a time in ms
    public static MidiEvent makeEventInstrumentChange(
            int instrumentId, int tick) throws InvalidMidiDataException {

        ShortMessage m = new ShortMessage();
        int channel = 1;
        m.setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrumentId);
        return new MidiEvent(m, tick);
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

    public static Sequence makeSequence(List<MidiEvent> events)
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
