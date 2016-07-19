package com.headfirst.midi.beatboxv2.sound;

import com.sun.media.sound.SoftSynthesizer;

import javax.sound.midi.Instrument;

/**
 * Created by Tom on 6/2/2016.
 */
/*
To select instruments beyond < 127, you need to set instrument bank too. This
 seems to possible only by getting hold of object MidiChannel. This object
 can be acquired from Synthesizer and set directly required MIDI events
 without constructing MidiEvent from ShortMessage.

 https://docs.oracle.com/javase/7/docs/api/javax/sound/midi/MidiChannel.html
 https://docs.oracle.com/javase/7/docs/api/javax/sound/midi/Patch.html

 */
public class Instruments {
    Instrument[] all;

    public Instruments() {
        SoftSynthesizer synth = new SoftSynthesizer();
        all = synth.getAvailableInstruments();
    }

    public String getByIndex(int index) {
        return all[index].getName();
    }

    public int getByName(String name) {
        for (int i = 0; i < all.length; i++) {
            if (all[i].getName().toLowerCase().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public void printInstrumentsMap() {
        System.out.println("[map");
        for (int i = 0; i < all.length; i++) {
            System.out.println(String.format("%d -> %s", i, all[i]));
        }
    }
}
