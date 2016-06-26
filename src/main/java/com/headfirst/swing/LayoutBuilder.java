package com.headfirst.swing;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Tom on 5/19/2016.
 */

/*
Class to play and see how to layout works with layout managers

Need to make very small iteration as it is hard to guess why layout look
different than you'd want !!

 */
public class LayoutBuilder extends JPanel {
    private java.util.List<String> randomNames = new ArrayList<>(Arrays.asList(
            "foo", "bar", "bob", "car", "sand", "red", "blue", "sun", "drum"
    ));
    private Random rand;

    public LayoutBuilder() {
        rand = new Random(System.currentTimeMillis());
        setBackground(Color.blue);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        makeWidgets();
        setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public static void main(String[] args) {
        LayoutBuilder builder = new LayoutBuilder();
        JFrame frame = new JFrame("Layout builder");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(builder);
//        frame.getContentPane().add(builder, BorderLayout.EAST);
        frame.setSize(500, 500);
        frame.setLocation(500, 500);
        frame.setVisible(true);
    }

    public void makeWidgets() {
        add(ButtonBarVertical(5));
        add(ButtonBarVertical(5));
        add(ButtonBarVertical(5));
        add(ButtonBarHorizontal(3));
    }

    public JPanel ButtonBarVertical(int size) {
        JPanel bar = new JPanel();
        bar.setLayout(new BoxLayout(bar, BoxLayout.Y_AXIS));
        String btnName;
        for (int i = 0; i < size; i++) {
            btnName = randomNames.get(rand.nextInt(randomNames.size()));
            bar.add(new JButton(btnName));
        }
        return bar;
    }

    public JPanel ButtonBarHorizontal(int size) {
        JPanel bar = new JPanel();
        bar.setLayout(new BoxLayout(bar, BoxLayout.X_AXIS));
        String btnName;
        for (int i = 0; i < size; i++) {
            btnName = randomNames.get(rand.nextInt(size));
            bar.add(new JButton(btnName));
        }
        return bar;
    }
}
