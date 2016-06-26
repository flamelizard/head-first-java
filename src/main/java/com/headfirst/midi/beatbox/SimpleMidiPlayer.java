package com.headfirst.midi.beatbox;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.List;

import static com.headfirst.midi.beatbox.MidiUtils.makeEvent;
import static com.headfirst.midi.beatbox.MidiUtils.makeEventSetInstrumentOnChannel;

/**
 * Created by Tom on 6/2/2016.
 */
class SimpleMidiPlayer {
    private Sequencer sequencer;

    SimpleMidiPlayer() throws MidiUnavailableException {
        sequencer = MidiSystem.getSequencer();
    }

    public Sequencer getSequencer() {
        return sequencer;
    }

    public void scaleTempo(double percentage) {
        float tempoFactor = sequencer.getTempoFactor();
        sequencer.setTempoFactor((float) (tempoFactor * percentage));
        System.out.println("[tempo factor] " + sequencer.getTempoFactor());
    }

    public void playBeatBoxSequence(List<SongChannel> channels) {
        try {
            MidiEvent[] events = convertToMidiEvents(channels)
                    .toArray(new MidiEvent[]{});

            Sequence sequence = MidiUtils.makeSequence(events);
            openSequencer();
            sequencer.setSequence(sequence);
        } catch (InvalidMidiDataException | MidiUnavailableException e) {
            e.printStackTrace();
            return;
        }
        sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        sequencer.start();  // returns immediately, stop with call to .stop()
    }

    private List<MidiEvent> convertToMidiEvents(
            List<SongChannel> channels) throws InvalidMidiDataException {

        List<MidiEvent> events = new ArrayList<>();
        MidiEvent event;
        int midiChannel;

        for (SongChannel ch : channels) {
//            each instrument gets own MIDI channel
            midiChannel = ch.getMidiChannel();
//            System.out.println("[channel] " + midiChannel);

            event = makeEventSetInstrumentOnChannel(
                    midiChannel, ch.getInstrument(), ch.getTicks().get(0));
            events.add(event);

            for (int tick : ch.getTicks()) {
                event = makeEvent(
                        ShortMessage.NOTE_ON, midiChannel, ch.getNote(),
                        ch.getVelocity(), tick);
                events.add(event);

                events.add(makeEvent(
                        ShortMessage.NOTE_OFF, midiChannel, ch.getNote(),
                        ch.getVelocity(), tick + 1));
            }
        }
        // force to play full sequence length
        int anyMidiChannel = 1;
        int anyInstrument = 1;
        int lastTick = BeatBox.sequenceLength - 1;
        event = makeEventSetInstrumentOnChannel(
                anyMidiChannel, anyInstrument, lastTick);
        events.add(event);

        return events;
    }

    public void openSequencer() throws MidiUnavailableException {
        if (sequencer != null) {
            sequencer.open();
        }
    }

    public void closeSequencer() {
        if (sequencer != null) {
            sequencer.close();
        }
    }

    //    quick play for testing purposes
    public void quickPlay(MidiEvent[] events) {
        System.out.println("Quick playing ...");
        Sequence sequence;
        try {
            sequence = MidiUtils.makeSequence(events);
            sequencer.setSequence(sequence);
            openSequencer();
        } catch (InvalidMidiDataException | MidiUnavailableException e) {
            e.printStackTrace();
            return;
        }

        sequencer.start();
    }
}
