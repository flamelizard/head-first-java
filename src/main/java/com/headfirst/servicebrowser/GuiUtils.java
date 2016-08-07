package com.headfirst.servicebrowser;

import javax.swing.*;

/**
 * Created by Tom on 8/3/2016.
 */
public class GuiUtils {
    public static void showInFrame(String name, JPanel panel) {
        JFrame frame = new JFrame(name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();   // set size automatically
        frame.setLocation(500, 300);
        frame.setVisible(true);
    }
}
