package com.headfirst.midi.beatbox;

import org.junit.Before;
import org.junit.Test;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 6/6/2016.
 */
public class MidiUtilsTest {
    private Instruments instruments;
    private SimpleMidiPlayer player;

    public MidiUtilsTest() throws MidiUnavailableException {
        instruments = new Instruments();
        player = new SimpleMidiPlayer();
    }

    @Before
    public void setUp() {

    }

    //    used for debugging why instrument does not change
    @Test
    public void playSomeInstruments() throws InvalidMidiDataException {
        int[] instrNums = {20, 40, 60};
        int tick = 0;
        MidiEvent event;
        List<MidiEvent> events = new ArrayList<>();

        for (int num : instrNums) {
            tick = 0;
            event = MidiUtils.makeEventSetInstrumentOnChannel(1, num, tick);
            events.add(event);
            event = MidiUtils.makeEvent(
                    ShortMessage.NOTE_ON, 1, 40, 100, tick);
            events.add(event);
            event = MidiUtils.makeEvent(
                    ShortMessage.NOTE_OFF, 1, 40, 100, tick + 3);
            events.add(event);

            System.out.println("Now playing " + instruments.getByIndex(num));
            player.quickPlay(events.toArray(new MidiEvent[]{}));
            events.clear();
        }
    }

}