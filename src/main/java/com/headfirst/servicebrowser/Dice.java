package com.headfirst.servicebrowser;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Tom on 7/27/2016.
 */
/*
dice face is a grid 3 x 3
 */
public class Dice extends JPanel {
    private final int MAX_VALUE = 6;
    private final Random rand;
    private Map<Integer, int[]> digitToDots = new HashMap<>();
    private Color background = Color.WHITE;
    private Color dotColor = Color.BLACK;

    public Dice() {
        rand = new Random(System.currentTimeMillis());
        initDigitToDots();
        add(getDiceFace(6));
        setBorder(BorderFactory.createDashedBorder(Color.BLUE));
    }

    private void initDigitToDots() {
        int mappingLen = 10;
        int[] mapping = {
                1, 0, 0, 0, 0, 1, 0, 0, 0, 0,
                2, 0, 1, 0, 0, 0, 0, 0, 1, 0,
                3, 1, 0, 0, 0, 1, 0, 0, 0, 1,
                4, 1, 0, 1, 0, 0, 0, 1, 0, 1,
                5, 1, 0, 1, 0, 1, 0, 1, 0, 1,
                6, 1, 1, 1, 0, 0, 0, 1, 1, 1
        };

        for (int i = 0; i < mapping.length; i = i + mappingLen) {
            digitToDots.put(mapping[i], Arrays.copyOfRange(
                    mapping, i + 1, i + mappingLen));
        }
//        for (Integer k: digitToDots.keySet()) {
//            System.out.println(k + "--" + Arrays.toString(digitToDots.get(k)));
//        }
    }

    private JPanel getDiceFace(int digit) {
        int[] digitLayout = digitToDots.get(digit);
        byte id = 0;
        JPanel face = new JPanel();
        face.setBackground(background);
        face.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        face.setLayout(new BoxLayout(face, BoxLayout.PAGE_AXIS));

        Color col;
        for (int i = 0; i < 3; i++) {
            Box row = Box.createHorizontalBox();
            for (int j = 0; j < 3; j++) {
                col = getDotColor(digitLayout[id++]);
                row.add(new Dot(col));
            }
            face.add(row);
        }
        return face;
    }

    private Color getDotColor(int num) {
        Color col = background;
        if (num == 1) {
            col = dotColor;
        }
        return col;
    }

    //    change dice face - remove/add JPanel
    public void rollTheDice() {
        int rolledNum = rand.nextInt(MAX_VALUE) + 1;
        removeAll();
        add(getDiceFace(rolledNum));
        revalidate();
    }

    class Dot extends JPanel {
        private final Point center;
        private Color color;
        private int diameter;
        private int size;

        public Dot(Color col) {
            color = col;
            diameter = 30;
            size = 40;
            if (diameter > size) {
                throw new IllegalArgumentException("Diameter too big");
            }
            center = new Point(size / 2 - diameter / 2, size / 2 - diameter / 2);
//            setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        public void paintComponent(Graphics g) {
            g.setColor(color);
            g.fillOval((int) center.getX(), (int) center.getY(),
                    diameter, diameter);
        }

        public Dimension getPreferredSize() {
            return new Dimension(size, size);
        }
    }
}
