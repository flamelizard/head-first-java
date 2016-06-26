package com.headfirst.swing;

import com.headfirst.midi.basics.BeatGenerator;

import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

/**
 * Created by Tom on 5/6/2016.
 */
/*
Beat Box GUI will draw random rectangles on every beat (tick) from
BeatGenerator class. Button start will play MIDI music sequence to its beats
rectangles are drawn.

Status: done
 */
public class BeatBoxGui {
    private JFrame win;
    private RectCanvas canvas;
    private BeatGenerator beatBox;

    public BeatBoxGui() {
        win = new JFrame("Music Box 0.1");
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        probably anonymous instance, many other methods to override
        win.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (beatBox != null) {
                    beatBox.closeSequencer();
                }
                super.windowClosing(windowEvent);
            }
        });
    }

    public static void main(String[] args) {
        BeatBoxGui gui = new BeatBoxGui();
        gui.drawOnBeats();
//        gui.drawOnBeatsAutomatic();
    }

    public void drawOnBeats() {
        makeWidgets();
        beatBox = new BeatGenerator();
        beatBox.registerWidgetForCtrlEvents(canvas);
        showGui();
    }

    void showGui() {
        win.setSize(400, 400);
        win.setLocation(500, 500);  // frame location on the screen
        win.setVisible(true);
    }

    void makeWidgets() {
        canvas = new RectCanvas();
        win.getContentPane().add(BorderLayout.CENTER, canvas);
        win.getContentPane().add(BorderLayout.SOUTH, new ButtonBar());
    }

    class ButtonBar extends JPanel {
        public ButtonBar() {
            super();
            JButton btn1 = new JButton("Start");
            JButton btn2 = new JButton("Stop");
            btn1.addActionListener(new PlayBeatBox());

            this.add(BorderLayout.WEST, btn1);
            this.add(BorderLayout.EAST, btn2);
        }
    }

    class PlayBeatBox implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                beatBox.playMidiSeq();
            } catch (InvalidMidiDataException e) {
                System.out.println("Invalid MIDI data sequence");
                e.printStackTrace();
            }
        }

    }

    //    Implement interface for MIDI Sequencer on Swing widget !!
    public class RectCanvas extends JPanel
            implements ControllerEventListener {

        private Random rand = new Random(System.currentTimeMillis());

        @Override
        public void controlChange(ShortMessage shortMessage) {
            drawRandomRectangle();
        }

        void drawRandomRectangle() {
            Graphics2D g = (Graphics2D) win.getGraphics();
            int pointsSpread = 50;
            int baseLength = 10;

            Point p1 = getRandomPointBounded(this.getWidth() / 2, pointsSpread);
            int width = baseLength + rand.nextInt(100);
            int height = baseLength + rand.nextInt(100);

            g.setColor(getRandomColor());
            g.fillRect(p1.x, p1.y, width, height);
        }

        Color getRandomColor() {
            return new Color(
                    rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        }

        public Point getRandomPointBounded(int min, int variance) {
//            variance is a tolerance on left and right side respectively
//            left and right boundary are inclusive
            int x = min - variance + rand.nextInt(2 * variance + 1);
            int y = min - variance + rand.nextInt(2 * variance + 1);
            return new Point(x, y);
        }

        public void drawOnBeatsAutomatic() {
            canvas = new RectCanvas();
            beatBox = new BeatGenerator();
            beatBox.registerWidgetForCtrlEvents(canvas);

            win.setContentPane(canvas);
            win.setBounds(30, 30, 300, 300);
            win.setVisible(true);

            try {
                System.out.println("Playing MIDI sequence");
                beatBox.playMidiSeq();
            } catch (InvalidMidiDataException e) {
            }
        }

        void packageOnlyMethod() {
            System.out.println("Access granted !!");
        }
    }
}
