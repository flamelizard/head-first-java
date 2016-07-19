package com.headfirst.midi.beatboxv2.chat;

/**
 * Created by Tom on 7/17/2016.
 */
// Subscribe to ChatClient in order to get all messages received byt it
public interface Subscriber {
    void processUpdate(ChatMessage msg);
}
