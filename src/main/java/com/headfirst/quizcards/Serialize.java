package com.headfirst.quizcards;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 6/21/2016.
 */
public class Serialize {
    public static void serializeCsv(File toFile, ArrayList<QuizCard> cards) {
        try {
//            efficient write with buffered class
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(toFile)
            );
            StringBuilder cardToCSV = new StringBuilder();
            for (QuizCard card : cards) {
                if (cardToCSV.length() > 0) {
                    cardToCSV.delete(0, cardToCSV.length());
                }
                cardToCSV.append(card.getQuestion()).append(",");
                cardToCSV.append(card.getAnswer()).append(",,");
                System.out.println("[csv] " + cardToCSV);
                writer.write(cardToCSV.toString());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void serialize(File toFile, Object[] objects)
            throws IOException {

        ObjectOutputStream outStream = new ObjectOutputStream(
                new FileOutputStream(toFile));
        for (Object o : objects) {
            outStream.writeObject(o);
        }
        outStream.close();
    }

    public static List<Object> deserializeAll(File fromFile)
            throws IOException {

        List<Object> objects = new ArrayList<>();
        try (FileInputStream fileIn = new FileInputStream(fromFile);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
//            risky loop that relies on IOException, idk better way
            while (true) {
                try {
                    objects.add(objectIn.readObject());
                } catch (IOException expected) {
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return objects;
    }

    public static Object[] deserialize(File fromFile, int numObjects) {
        Object[] objects = new Object[numObjects];
        try {
            ObjectInputStream inStream = new ObjectInputStream(
                    new FileInputStream(fromFile)
            );
            for (int i = 0; i < numObjects - 1; i++) {
                objects[i] = inStream.readObject();
            }
            inStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return objects;
    }
}
