package com.headfirst.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Tom on 4/22/2016.
 */

/*
"slf4j" is abstraction layer that support using whatever logging framework
It has advantages over using pure "log4j"

However, this is useful for library and embedded components and not
necessarily for stand-alone application that can invoke logging framework
directly.

Well, I can stick with "slf4j" using "log4j", so I can take advantage of
parametrized message. If I like it, I might learn "logback" that is a successor
 to "log4j". "logback" uses natively "slf4j" but has some configuration
 differences from "log4j".

Requires two jars:
slf4j-api
binding jar for the wanted logging framework, e.g slf4j-log4j
*/
public class LogWithSLF4J {
    static final Logger log = LoggerFactory.getLogger(LogWithSLF4J.class);

    public static void main(String[] args) {
        log.info("logging with SLF4J");
        log.info("parametrized message - {} {}", "low", "overhead");
    }
}
