package com.headfirst;

/**
 * Created by Tom on 3/15/2016.
 */
public class InterfaceExtendsLesson {

    // public abstract implicitly
    interface Cipherable {
        void cipher();

        void decipher();

        boolean checkIntegrity();
    }

    class Record {
        void fillIn() {
        }

        public boolean checkIntegrity() {
            System.out.println("[record has not be tempered]");
            return true;
        }
    }

    /*
    Concrete implementations of interface methods have to be public since
    interface and its methods are always implicitly "public abstract". No need
    to type explicitly.
    */
    class SecretRecord extends Record implements Cipherable {
        public void cipher() {
        }

        public void decipher() {
        }
//    third method is implemented in super class
    }
}


