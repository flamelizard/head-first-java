package com.headfirst.logging;

import org.apache.log4j.Logger;

/**
 * Created by Tom on 4/21/2016.
 */
public class LogWithLog4j {
    static Logger log = Logger.getLogger(LogWithLog4j.class);

    public static void main(String[] args) {
//        Appender append = new org.apache.log4j.ConsoleAppender();
//        log.addAppender(append);

//        simple no file config for logging to console
//        BasicConfigurator.configure();
//        log.setLevel(Level.DEBUG);

//      log4j.properties has to be in classpath, dir for maven project is
//      src/main/resources
        log.debug("debug shout");
        log.info("info message");
    }
}
