package com.headfirst.midi.beatbox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 6/5/2016.
 */
/*
Class that fully describes single song channel - instrument, note, times
when instrument should be played.
This information is extracted from BeatBoxHFJ switchboard panel.
 */
class SongChannel {
    private int instrument;
    private int note;
    private List<Integer> ticks;
    private int velocity = 100;

    public int getMidiChannel() {
        return midiChannel;
    }

    //    MIDI channel allows playing up to 16 instruments simultaneously
    private int midiChannel = 1;

    SongChannel(int instrument, int note) {
        this.instrument = instrument;
        this.note = note;
        ticks = new ArrayList<>();
    }

    SongChannel() {
        this(1, 40);
    }

    public void setMidiChannel(int midiChannel) {
        if (midiChannel > 15) {
            throw new IllegalArgumentException("MIDI channel can be 0-15");
        }
        this.midiChannel = midiChannel;
    }

    public int getVelocity() {
        return velocity;
    }

    public int getInstrument() {
        return instrument;
    }

    public int getNote() {
        return note;
    }

    public List<Integer> getTicks() {
        return ticks;
    }

    public void addTick(int tick) {
        ticks.add(tick);
    }

    public void offsetTicks(int offset) {
        for (int i = 1; i < ticks.size(); i++) {
            ticks.set(i, ticks.get(i) + offset);
        }
    }

    public String toString() {
        Instruments instr = new Instruments();
        return String.format("%s: note %s, ticks %s",
                instr.getByIndex(getInstrument()), getNote(), getTicks()
                        .toString());
    }
}
