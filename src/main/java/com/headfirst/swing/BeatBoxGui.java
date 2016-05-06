package com.headfirst.swing;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by Tom on 5/6/2016.
 */
public class BeatBoxGui {
    JFrame win;

    public BeatBoxGui() {
        win = new JFrame("Music Box 0.1");
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        BeatBoxGui gui = new BeatBoxGui();
        gui.show();
    }

    public void show() {
        makeWidgets();

        win.setSize(400, 400);
        win.setLocation(500, 500);  // frame location on the screen
        win.setVisible(true);
    }

    public void makeWidgets() {
        win.getContentPane().add(BorderLayout.CENTER, new RectCanvas());
        win.getContentPane().add(BorderLayout.SOUTH, new ButtonBar());
    }

    class ButtonBar extends JPanel {

        public ButtonBar() {
            super();
            JButton btn1 = new JButton("Start");
            JButton btn2 = new JButton("Stop");

            this.add(BorderLayout.WEST, btn1);
            this.add(BorderLayout.EAST, btn2);
        }
    }

    class RectCanvas extends JPanel {
        private final Point start = new Point(50, 50);
        private final byte maxLen = 10;
        private Random rand = new Random(System.currentTimeMillis());

        public void paintComponent(Graphics g) {
            int x = rand.nextInt(maxLen);
            int y = rand.nextInt(maxLen);

            g.setColor(getRandomColor());
            g.fillRect(x, y, (int)start.getX()-x, (int)start.getY()-y);
        }

        public Color getRandomColor() {
            return new Color(
                    rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        }
    }
}
