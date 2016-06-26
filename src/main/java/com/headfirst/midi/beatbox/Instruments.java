package com.headfirst.midi.beatbox;

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
        System.out.println("[map");
        for (int i = 0; i < all.length; i++) {
            System.out.println(String.format("%d -> %s", i, all[i]));
        }
    }
}
