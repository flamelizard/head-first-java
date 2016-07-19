package com.headfirst.midi.beatboxv2.chat;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tom on 7/10/2016.
 */
/*
BeatBox message sent over socket

Explicit UID will make ChatMessage objects backward compatible with objects
created from older ChatMessage version. However, it is up to author to
ensure nothing breaks when object is deserialized from old class to newer class
version.
 */
public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 4128849050555146448L;
    private String comment;
    private List<Boolean> beatBoxPattern;

    public ChatMessage(String comment, List<Boolean> pattern) {
        this.comment = comment;
        beatBoxPattern = pattern;
    }

    public String getComment() {
        return comment;
    }

    public List<Boolean> getBeatBoxPattern() {
        return beatBoxPattern;
    }

    public String toString() {
        return getComment();
    }
}
