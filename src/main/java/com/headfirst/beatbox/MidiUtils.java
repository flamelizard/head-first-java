package com.headfirst.beatbox;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import java.util.Arrays;

/**
 * Created by Tom on 5/2/2016.
 */
public class MidiUtils {

    //    helper func to properly create MIDI event
    public static MidiEvent makeEvent
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

    public static void printEvent(MidiEvent ev) {
        System.out.println("[Midi event]");
        System.out.println(Arrays.toString(ev.getMessage().getMessage()));
    }

}
